import java.util.concurrent.BlockingQueue

/**
 * Created by bensoer on 02/02/17.
 */
class PrimeNumberGenerator(private val blockingQueue: BlockingQueue<Long>) : Runnable {

    override fun run() {
        try{
            println("PrimeNumberGenerator: Generator Has Started")

            outer@ while(true){

                //FIXME: Reduce the amount of locks needed to start

                //get the number
                val number = NextNumberGenerator.getNextNumber() // lock 1
                //find its square root, cast to int value
                val root = Math.round(Math.sqrt(number.toDouble()))
                //get a copy of all primes found thus far
                val previousPrimes = NextNumberGenerator.getPreviousPrimesUpToRoot(root) // lock 2

                val iterator = previousPrimes.iterator()
                while(iterator.hasNext()){
                    val next = iterator.next()
                    if(next <= root){

                        if(number % next == 0.toLong()){
                            continue@outer
                        }

                    }
                }

                //this number is a prime!
                blockingQueue.add(number)
                NextNumberGenerator.addPrime(number)
            }
        }catch(ie: InterruptedException){
            println("PrimeNumberGenerator: Terminating")
        }
    }
}