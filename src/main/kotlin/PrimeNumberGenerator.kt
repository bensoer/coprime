import java.util.concurrent.BlockingQueue

/**
 * Created by bensoer on 02/02/17.
 */
class PrimeNumberGenerator(private val blockingQueue: BlockingQueue<Long>) : Runnable {


    private var number:Long = 3

    override fun run() {
        try{
            println("PrimeNumberGenerator: Generator Has Started")

            outer@ while(true){

                for(i in 2..(number-1)){
                    if(number % i == 0.toLong()){
                        //this is not a prime number
                        number++
                        continue@outer
                    }
                }

                //this number is a prime!
                blockingQueue.add(number)
                number++
            }
        }catch(ie: InterruptedException){
            println("PrimeNumberGenerator: Terminating")
        }
    }

}