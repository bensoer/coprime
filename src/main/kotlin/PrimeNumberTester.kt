import java.util.concurrent.Callable

/**
 * Created by bensoer on 02/02/17.
 */
class PrimeNumberTester(private val possiblePrimeNumber:Long) : Callable<Boolean> {


    override fun call(): Boolean {

        for(i in 2..(possiblePrimeNumber-1)){
            if(possiblePrimeNumber % i == 0.toLong()){
                //this is not a prime number
                return false
            }
        }

        return true

    }

}