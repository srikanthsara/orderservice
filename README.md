# orderservice
orderservice


### Go to cartservice:  http://localhost:8763/cartservice/swagger-ui/index.html

### Go to API cart/add

{
    
	"customerId": "CUST1002",
    "customerName": "Srikanth",
    "productId": "GROC1003",
    "productName": "India Gate Basmati Rice 25KG",
    "category": "Grocery",
    "brand": "India Gate",
    "quantity": 2,
    "unitPrice": 2450
	
}

### Go to swagger url of Order Service :http://localhost:8764/orderservice/swagger-ui/index.html

### POST /orders/checkout

{
  
	"customerId": "CUST1002",

	"customerName": "Srikanth",

	"customerEmail": "srikanth@gmail.com"
  
}

### Response Body

{
	  
	  "orderId": 12,
	  "customerId": "CUST1002",
	  "customerName": "Srikanth",
	  "customerEmail": "srikanth@gmail.com",
	  "subTotal": 4900,
	  "gstAmount": 882,
	  "shippingCharges": 100,
	  "discountAmount": 200,
	  "totalAmount": 5682,
	  "paymentStatus": "PENDING",
	  "inventoryStatus": "PENDING",
	  "orderStatus": "ORDER_CREATED",
	  "createdAt": "2026-05-26T00:38:45.6918123",
	  "orderItems": [
		{
		  "orderItemId": 10,
		  "productId": "GROC1003",
		  "productName": "India Gate Basmati Rice 25KG",
		  "quantity": 2,
		  "unitPrice": 2450,
		  "totalPrice": 4900
		}
	  ]
	  
}
