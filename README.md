#coprime
Cryptools is a simple Kotlin terminal application that generates and tests various components needed or related
in generating RSA public and private keys. This includes, prime and co-prime number generation and testing, public key
and private key generation, and some cracking tools including prime factorization for cracking primes of a totient,
and private key cracking by using components of the public key

#Setup
##Prerequisites
You will need the latest Java 8 installed. The program has only been tested using the Oracle JDK.
Additionally, Java is assumed to be available in your path and available in terminal for the installation procedure

NOTE: Kotlin is not required in order to compile and execute the program. Gradle will handle all dependencies other
then Java 8

##Installation
1. Clone the repo
2. `cd` to the project root
3. Execute `./gradlew mainJar` for linux or `./gradlew.bat mainJar` for windows
4. Navigate from the project root to `./build/libs/`
5. Execute `java -jar ./cryptools-1.0.0.jar` an you will be prompted with usage instructions