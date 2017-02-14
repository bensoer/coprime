/**
 * Created by bensoer on 09/02/17.
 */
class PublicKeyGenerator(firstPrimeNumber:Long, secondPrimeNumber:Long) {

    val n:Long = firstPrimeNumber * secondPrimeNumber
    val e:Long?

    val totient = (firstPrimeNumber - 1)*(secondPrimeNumber -1)

    // C = m^e mod n
    // WHERE:
    //  C = ciphertext
    //  m = plaintext
    // e and n make up the public key. See ASN.1 to format these numbers into a key file

    init{

        //generate the E value. E < totient and E > biggest(p,q). E should also be CoPrime with the totient
        if(firstPrimeNumber > secondPrimeNumber){
            e = CoPrimeNumberGenerator.findCoPrimeToTotient(totient, firstPrimeNumber)
        }else{
            e = CoPrimeNumberGenerator.findCoPrimeToTotient(totient, secondPrimeNumber)
        }

        //calculate d value
        //println(EuclideanGCDGenerator.findExtendedGCD(totient, e!!))
    }


}