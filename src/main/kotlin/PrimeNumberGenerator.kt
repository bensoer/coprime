import java.util.concurrent.BlockingQueue

/**
 * Created by bensoer on 02/02/17.
 */
class PrimeNumberGenerator(private val blockingQueue: BlockingQueue<Long>) : Runnable {

    override fun run() {
        try{
            println("PrimeNumberGenerator: Generator Has Started")

            outer@ while(true){

                val number = NextNumberGenerator.getNextNumber()

                for(i in 2..(number-1)){
                    if(number % i == 0.toLong()){
                        //this is not a prime number
                        continue@outer
                    }
                }
                //this number is a prime!
                blockingQueue.add(number)
            }
        }catch(ie: InterruptedException){
            println("PrimeNumberGenerator: Terminating")
        }
    }

}