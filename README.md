## simple-jwt-rest-template

Here template REST application using Java Spring Boot + JWT+Postgre

## Installation

---

1. clone this source
2. create database
3. build and run

---

## LIST API

#### 1. SignUp

##### POST /api/auth/signup

**body**

```json
{
  "email": "anwar@gmail.com",
  "password": "1234567"
}
```

**response**

```json
{
  "success": true,
  "errorCode": null,
  "message": "User registered successfully. Please login",
  "payloads": null
}
```

---

#### 2. SignIn

##### POST /api/auth/signin

**body**

```json
{
  "email": "anwar@gmail.com",
  "password": "1234567"
}
```

**response**

```json
{
  "success": true,
  "errorCode": null,
  "message": "token generated",
  "payloads": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNTQwOTA3Nzk5LCJleHAiOjE1NDE1MTI1OTl9.pxu2epiXnXWKgzbA10RBpQpn6IF_57mzIVLZrsItOc6jmv8sTT2hzgynpvIsXqyCj054oj33PV84WEXPOXWM4Q",
    "tokenType": "Bearer",
    "lastLogin": null
  }
}
```

---

#### 3. Deposit

##### POST /api/trx/deposit

**header**
Authorization : Bearer [token]

**body**

```json
{
  "amount": 100,
  "remark": "terminal_id|merchant_id|etc"
}
```

**response**

```json
{
  "success": true,
  "errorCode": null,
  "message": "deposit success",
  "payloads": null
}
```

---

#### 4. Withdrawal

##### POST /api/trx/withdrawal

**header**
Authorization : Bearer [token]

**body**

```json
{
  "amount": 10,
  "otp": "123456",
  "remark": "terminal_id|merchant_id|etc"
}
```

**response**

```json
{
  "success": true,
  "errorCode": null,
  "message": "withdrawal success",
  "payloads": null
}
```

---

#### 5. Balance

##### GET /api/user/balance

**header**
Authorization : Bearer [token]

**response**

```json
{
  "success": true,
  "errorCode": null,
  "message": "",
  "payloads": {
    "currency": "USD",
    "balance": 190,
    "transactions": [
      {
        "id": 10,
        "side": "CREDIT",
        "type": "DEPOSIT",
        "amount": 100,
        "trxDate": "2018-10-30T13:56:28.654+0000",
        "remark": "initiate"
      },
      {
        "id": 11,
        "side": "CREDIT",
        "type": "DEPOSIT",
        "amount": 100,
        "trxDate": "2018-10-30T13:57:00.814+0000",
        "remark": "terminal_id|merchant_id|etc"
      },
      {
        "id": 12,
        "side": "DEBET",
        "type": "WITHDRAWAL",
        "amount": 10,
        "trxDate": "2018-10-30T13:57:14.763+0000",
        "remark": "terminal_id|merchant_id|etc"
      }
    ]
  }
}
```
