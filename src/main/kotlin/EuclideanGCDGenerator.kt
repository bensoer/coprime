import java.util.concurrent.Callable

/**
 * Created by bensoer on 02/02/17.
 */
class EuclideanGCDGenerator(private val numberA:Long, private val numberB:Long) : Callable<Long> {


    override fun call(): Long {
        return findGCD(numberA, numberB)
    }


    private fun findGCD(numberA:Long, numberB:Long):Long{

        if(numberA == 0.toLong()){
            return numberB
        }

        if(numberB == 0.toLong()){
            return numberA
        }

        //val numberAQuotient = numberA / numberB
        val numberARemainder = numberA % numberB

        //A = B*Q + R
        // numberA = numberB * numberAQuotient + numberARemainder

        //find GCD(B,R) since GCD(A,B) == GCD(B,R)

        return findGCD(numberB, numberARemainder)
    }


    companion object{
        //python version
        data class Results(val gcd:Long, val x:Long, val y:Long)
        fun findExtendedGCD(numberA:Long, numberB:Long):Results{

            if(numberA == 0.toLong()){
                return Results(numberB, 0.toLong(), 1.toLong())
            }else{

                val results = findExtendedGCD(numberB % numberA, numberA)
                return Results(results.gcd, results.y - (numberB / numberA) * results.x, results.x)
            }
        }
    }

}