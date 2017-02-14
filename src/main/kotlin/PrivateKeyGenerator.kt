/**
 * Created by bensoer on 09/02/17.
 */
class PrivateKeyGenerator(val totient:Long, e:Long) {

    //NOTE: totient == n

    val d:Long = EuclideanGCDGenerator.findExtendedGCD(totient, e).y

}