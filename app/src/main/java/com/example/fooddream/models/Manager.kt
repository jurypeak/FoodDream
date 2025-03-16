package com.example.fooddream.models

class Manager(
    email: String,
    accountId: Int,
    accessLevel: Int,
    password: String
) : Account(
    email,
    accountId,
    accessLevel,
    password
)