package classes.network.dto

class AuthResponse {
    var jwtToken: String? = null

    constructor() {}
    constructor(jwtToken: String?) {
        this.jwtToken = jwtToken
    }
}