package com.justnik.mosplace.data.network.authmodel

class UserFullInfo(email: String, password: String, val username: String) :
    UserBriefInfo(email, password) {
}