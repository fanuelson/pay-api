import { check } from 'k6';
import http from 'k6/http';
import {
  randomString,
  randomIntBetween
} from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

const host = 'localhost';
const port = '8080';
const endpoint = '/transactions/transfer/list'

export default function () {
    let payload = JSON.stringify(
    {
    	"transactions": [
    		{
    			"payer": 1,
    			"payee": 2,
    			"amount": 1
    		},
    		{
    			"payer": 4,
    			"payee": 2,
    			"amount": 1
    		}
    	]
    }
    );

    let params = { headers: { "Content-Type": "application/json" } };
    let res = http.post(`http://${host}:${port}${endpoint}`, payload, params);

    check(res, { 'is status 201': (r) => r.status === 201 });
}