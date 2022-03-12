package classes.network.dto

class AuthRequest {
    var login: String? = null
    var password: String? = null

    constructor(login: String, password: String){
        this.login = login
        this.password = password
    }
}