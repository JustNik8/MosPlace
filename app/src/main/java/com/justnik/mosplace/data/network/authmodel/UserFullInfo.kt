package com.justnik.mosplace.data.network.authmodel

class UserFullInfo(username: String, password: String, val email: String) :
    UserBriefInfo(username, password) {
}