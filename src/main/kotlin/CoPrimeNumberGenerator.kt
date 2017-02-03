import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService

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


    override fun run() {
        println("CoPrimeNumberGenerator: Generator Has Started")

        while(true){
            //println("CoPrimeNumberGenerator: Generating Callables For CoPrime Number Combinations For Range $numberB To $currentFlipMax")
            val callables = ArrayList<CoPrimeTester>().toMutableList()

            //add all callables up to the flipmax
            while(numberB <= currentFlipMax){
                callables.add(CoPrimeTester(numberA, numberB))
                numberB++
            }

            //println("CoPrimeNumberGenerator: Now Executing All Callables")
            val futuresList = executor.invokeAll(callables)

            //println("CoPrimeNumberGenerator: Now Parsing All Results")
            futuresList.forEachIndexed { i, future ->
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

}