import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

val threadPoolSize = 5
val executor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)

fun printHelp(){

    println("CrypTool - Written By Ben Soer")
    println("\tVersion 1.0.0")
    println("\tBasic Usage: java -jar ./cryptools-1.0.0.jar -m GENPRIME|GENCOPRIME|GENCOPRIMEPRIME|TESTPRIME|TESTCOPRIME|PRIMEFACT " +
            "[-n factorizationNumber] [-p primeNumber] [-c1 coprime1] [-c2 coprime2]")
    println("\tParameters:")
    println("\t\t-m\t\tSpecifies The Mode. This can be either values GENPRIME, GENCOPRIME, TESTPRIME, or TESTCOPRIME")
    println("\t\t-p\t\tSpecify the number to be tested whether it is prime. Required when using the TESTPRIME mode")
    println("\t\t-c1\t\tSpecifies the first number to be tested in whether it is a coprime with -c2. Required when using TESTCOPRIME")
    println("\t\t-c2\t\tSpecifies the second number to be tested in whether it is a coprime with -c1. Required when using TESTCOPRIME")
    println("\t\t-n\t\tSpecifies the number to be prime factorized. Required when using PRIMEFACT")
    println("\tModes:")
    println("\t\tGENPRIME\t\t Generates Prime Numbers Starting At 0")
    println("\t\tGENCOPRIME\t\t Generated CoPrime Numbers starting At 0. Calculates in batches of 100")
    println("\t\tGENCOPRIMEPRIME\t\t Genrates CoPrime Numbers that are themselves prime numbers starting At 0. Calculates in batches of 100")
    println("\t\tTESTPRIME\t\t Tests the passed in number whether it is prime or not. Primal number is passed with -p")
    println("\t\tTESTCOPRIME\t\t Tests the passed in numbers whether they are coprime or not. CoPrime numbers are passed with -c1 and -c2")
    println("\t\tPRIMEFACT\t\t A Prime Factorization tool. Takes parameter -n and then generates all prime numbers whose product equals to n")

}

fun generatePrimeNumbers(){

    val blockingQueue = LinkedBlockingQueue<Long>()

    //for(i in 1..threadPoolSize){
        executor.submit(PrimeNumberGenerator(blockingQueue))
    //}

    while(true){
        val primeNumber = blockingQueue.take()
        println("Prime Number: $primeNumber")
    }
}

fun generateCoPrimeNumbers() {

    val coprimeExecutor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)
    val blockingQueue = LinkedBlockingQueue<Pair<Long, Long>>()

    val future = executor.submit(CoPrimeNumberGenerator(coprimeExecutor, blockingQueue))

    while (true){
        val coPrimePair = blockingQueue.take()
        println("CoPrime Numbers: ${coPrimePair.first} , ${coPrimePair.second}")
    }
}

fun generateCoPrimePrimes(){

    val coprimeExecutor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)
    val blockingQueue = LinkedBlockingQueue<Pair<Long, Long>>()

    val future = executor.submit(CoPrimeNumberGenerator(coprimeExecutor, blockingQueue))

    while (true){
        val coPrimePair = blockingQueue.take()
        if(isPrimNumber(coPrimePair.first) && isPrimNumber(coPrimePair.second)){
            println("CoPrime Prime Numbers: ${coPrimePair.first} , ${coPrimePair.second}")
        }
    }
}

fun generateCoPrimePrimesProductToN(n:Long){

    val coprimeExecutor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)
    val blockingQueue = LinkedBlockingQueue<Pair<Long, Long>>()

    val coprimeGenerator = CoPrimeNumberGenerator(coprimeExecutor, blockingQueue)
    val future = executor.submit(coprimeGenerator)

    while (true){
        val coPrimePair = blockingQueue.take()
        if(coPrimePair.first > n && coPrimePair.second > n){
            //its impossible for there to be anything else. stop now
            println("CoPrime Generator Is Using Values Large then N. Impossible To Generate Anymore CoPrime Primes With A Product Of N")
            coprimeGenerator.terminate = true
            future.cancel(true)
            return
        }
        if(isPrimNumber(coPrimePair.first) && isPrimNumber(coPrimePair.second) && (coPrimePair.first * coPrimePair.second) == n){
            println("CoPrime Prime Numbers: ${coPrimePair.first} , ${coPrimePair.second} Have A Product Of $n")
        }
    }


}

fun isPrimNumber(possiblePrimeNumber:Long):Boolean{

    val future = executor.submit(PrimeNumberTester(possiblePrimeNumber))
    return future.get()
}

fun isCoPrimeNumbers(numberA:Long, numberB:Long):Boolean{

    val future = executor.submit(CoPrimeTester(numberA, numberB))
    return future.get()
}

fun main(args: Array<String>){

    if(args.isEmpty()){
        printHelp()
        return
    }else{

        //parse args to figure out what to do
        val mode = ArgParser.getKeyValue(args, "-m")
        if(mode == null){
            printHelp()
            return
        }

        var modeEnum:ModeEnum? = null
        try{
            modeEnum = ModeEnum.valueOf(mode.toUpperCase())
        }catch(iae:IllegalArgumentException){
            printHelp()
            return
        }

        when(modeEnum!!){

            ModeEnum.TESTPRIME ->{

                try{
                    val prime = ArgParser.getKeyValue(args, "-p")!!.toLong()

                    if(isPrimNumber(prime)){
                        println("Main: $prime Is A Prime Number")
                    }else{
                        println("Main: $prime Is Not A Prime Number")
                    }
                }catch(knp: KotlinNullPointerException){
                    println("Main: A Prime Number Must Be Supplied To Test")
                    printHelp()

                }catch(nfe: NumberFormatException){
                    println("Main: The Supplied Value Is Not A Valid Number")
                    printHelp()
                }
            }

            ModeEnum.TESTCOPRIME ->{

                try{
                    val coprime1 = ArgParser.getKeyValue(args, "-c1")!!.toLong()
                    val coprime2 = ArgParser.getKeyValue(args, "-c2")!!.toLong()

                    if(isCoPrimeNumbers(coprime1, coprime2)){
                        println("Main: $coprime1 And $coprime2 Are CoPrime Numbers")
                    }else{
                        println("Main: $coprime1 And $coprime2 Are Not CoPrime Numbers")
                    }

                }catch(knp: KotlinNullPointerException){
                    println("Main: Two CoPrime Numbers Must Be Supplied To Test")
                    printHelp()

                }catch(nfe: NumberFormatException){
                    println("Main: The Supplied Values Are Not Valid Numbers")
                    printHelp()
                }

            }

            ModeEnum.GENPRIME ->{
                println("Main: Initializing Prime Number System")
                generatePrimeNumbers()
            }

            ModeEnum.GENCOPRIME ->{
                println("Main: Initializing CoPRime Number System")
                generateCoPrimeNumbers()
            }

            ModeEnum.GENCOPRIMEPRIME ->{
                println("Main: Initilizing CoPrimePrime Number System")
                generateCoPrimePrimes()
            }

            ModeEnum.PRIMEFACT ->{
                println("Main: Initializing Prime Factorization System")

                try{
                    val primeFactor = ArgParser.getKeyValue(args, "-n")!!.toLong()
                    generateCoPrimePrimesProductToN(primeFactor)

                }catch(knp: KotlinNullPointerException){
                    println("Main: Numbers Must Be Supplied To Test")
                    printHelp()

                }catch(nfe: NumberFormatException){
                    println("Main: The Supplied Value Is Not Valid")
                    printHelp()
                }
            }
        }
    }

    executor.shutdown()
}


