import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Created by bensoer on 02/02/17.
 */
class CoPrimeNumberGenerator(private val executor:ExecutorService, private val blockingQueue: BlockingQueue<Pair<Long,Long>>) :Runnable {


    //go to this max number before incrementing the second number
    //when both numbers are the flipMax increment flipMax += flipMax

    private val flipMax:Long = 100
    private var currentFlipMax:Long = flipMax
    private var flipMin:Long = 0

    private var numberA:Long = 0
    private var numberB:Long = 0

    var terminate = false

    override fun run() {
        println("CoPrimeNumberGenerator: Generator Has Started")

        outer@while(!terminate){
            //println("CoPrimeNumberGenerator: Generating Callables For CoPrime Number Combinations For Range $numberB To $currentFlipMax")
            val callables = ArrayList<CoPrimeTester>().toMutableList()

            //add all callables up to the flipmax
            while(numberB <= currentFlipMax){
                callables.add(CoPrimeTester(numberA, numberB))
                numberB++
            }

            if(terminate){
                executor.shutdown()
                executor.shutdownNow()
                return
            }

            //println("CoPrimeNumberGenerator: Now Executing All Callables")
            val futuresList = ArrayList<Future<Boolean>>()
            callables.forEach{
                futuresList.add(executor.submit(it))

            }
            //val futuresList = executor.invokeAll(callables)

            if(terminate){
                futuresList.forEach{
                    it.cancel(true)
                }
                executor.shutdown()
                executor.shutdownNow()
                return
            }

            //println("CoPrimeNumberGenerator: Now Parsing All Results")

            futuresList.forEachIndexed { i, future ->
                if(terminate){
                    future.cancel(true)
                    executor.shutdown()
                    executor.shutdownNow()
                    return
                }
                if(future.get()){
                    blockingQueue.add(Pair(callables[i].numberA, callables[i].numberB))
                }
            }
            //println("CoPrimeNumberGenerator: Incrementing Ranges Before Next Cycle")

            if(numberA >= currentFlipMax){

                flipMin = currentFlipMax
                currentFlipMax += flipMax


            }else if(numberB >= currentFlipMax){

                numberA++
                numberB = flipMin
            }
        }


    }

    companion object{

        fun findCoPrimeToTotient(totient:Long, startValue:Long):Long?{

            //count up from the start value to 1 before the totient to find a coprime
            for(i in startValue..(totient-1)){

                if(CoPrimeTester(i, totient).call()){
                    return i
                }

            }

            return null //couldn't find a coprime :s
        }
    }


}