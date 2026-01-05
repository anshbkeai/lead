from faker import Faker
import httpx as client
import json
import random
faker = Faker()

for i in range(1000):
    data = {
        "userId":faker.user_name(),
        "score":random.randint(10 ,10000)
    }
    resp = client.post("http://localhost:8080/api/score" , json=(data))