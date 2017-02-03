import java.util.concurrent.Callable

/**
 * Created by bensoer on 02/02/17.
 */
class CoPrimeTester(val numberA:Long, val numberB:Long) : Callable<Boolean> {

    override fun call(): Boolean {

        val gcdGenerator = EuclideanGCDGenerator(numberA, numberB)
        return gcdGenerator.call() == 1.toLong()
    }
}