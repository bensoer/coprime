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

        val numberAQuotient = numberA / numberB
        val numberARemainder = numberA % numberB

        //A = B*Q + R
        // numberA = numberB * numberAQuotient + numberARemainder

        //find GCD(B,R) since GCD(A,B) == GCD(B,R)

        return findGCD(numberB, numberARemainder)
    }


    //python version
    data class Results(val gcd:Float, val x:Float, val y:Float)
    private fun findExtendedGCD(numberA:Float, numberB:Float):Results{

        if(numberA == 0.toFloat()){
            return Results(numberB, 0.toFloat(), 1.toFloat())
        }else{

            val results = findExtendedGCD(numberB % numberA, numberA)
            return Results(results.gcd, results.y - (numberB / numberA) * results.x, results.x)
        }
    }


}