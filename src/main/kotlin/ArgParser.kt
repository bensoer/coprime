/**
 * Created by bensoer on 02/02/17.
 */
object ArgParser {

    fun getKeyValue(args:Array<String>,key:String):String?{

        args.forEachIndexed { index, string ->

            if(string.toLowerCase() == key.toLowerCase()){
                return args[index + 1]
            }
        }

        return null
    }
}