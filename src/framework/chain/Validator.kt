package framework.chain

interface Validator<T> {

    // Used to validate a particular transaction type
    // Each transaction type should have it's own validator
    fun validate(obj: T)
}