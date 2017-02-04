import java.util.concurrent.locks.ReentrantLock

/**
 * Created by bensoer on 03/02/17.
 */
object NextNumberGenerator {

    private var number:Long = 2
    private val lock = ReentrantLock()


    fun getNextNumber():Long{
        lock.lock()

        //increment to get the next number
        number++

        //then fetch a copy
        val copy = number

        //release lock
        lock.unlock()

        //return
        return copy
    }
}