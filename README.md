# Api Challenge

Project done with Java 11, PostgreSQL 12.9 and Spring Boot 2.6. Deployed on an instance AWS EC2 instance.

## Installation

Clone this repository

```bash
git clone git@github.com:vilteali/api-meli.git
```

## Create /api database and change the config
application.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/api?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8&amp;amp;autoReconnect=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Run and test with Postman

## 🚀 API Reference

```bash
https://api-meli-vilte.herokuapp.com
```

#### Validate if it's a mutant

```bash
POST /api/mutant
```
- Request as example json body

{ "dna": ["ATGCGA", "CTGTGC", "TTATGT", "ATAAGG", "CCGCTA", "TCACTG"] }

#### Get dna statistics

```bash
GET /api/stats
```
- Response example:

{ "countMutantDna": 1, "countHumanDna": 1, "ratio": 1.00 }
