import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by bensoer on 03/02/17.
 */
object NextNumberGenerator {

    private var number:Long = 1 // start here since auto increment is by 2
    private val lock = ReentrantLock()


    private val previousPrimesLock = ReentrantLock()
    private val previousPrimes = ArrayList<Long>()

    fun getNextNumber():Long{
        lock.lock()

        //increment to get the next number
        number += 2 // go by two cuz even numbers will never be primes

        //then fetch a copy
        val copy = number

        //release lock
        lock.unlock()

        //return
        return copy
    }

    fun addPrime(prime:Long){
        previousPrimesLock.lock()
        previousPrimes.add(prime)
        previousPrimes.sort()
        previousPrimesLock.unlock()
    }

    fun getPreviousPrimesUpToRoot(root:Long):ArrayList<Long>{
        previousPrimesLock.lock()
        val copy = ArrayList<Long>(previousPrimes.filter{ it <= root}.sorted())
        previousPrimesLock.unlock()
        return copy
    }


}