import pytest
import requests
import json
from datetime import datetime, timedelta
import random
import string
import logging
from pathlib import Path

# Setup logging
log_dir = Path("api_logs")
log_dir.mkdir(exist_ok=True)
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(message)s',
    handlers=[
        logging.FileHandler(log_dir / f"api_test_{datetime.now().strftime('%Y%m%d_%H%M%S')}.log"),
    ]
)
logger = logging.getLogger(__name__)

BASE_URL = "http://localhost:8080/api"

def log_request_response(method, url, request_data=None, response=None, error=None):
    """Log API request and response details"""
    log_entry = {
        "timestamp": datetime.now().isoformat(),
        "method": method,
        "url": url,
        "request_data": request_data,
        "response_status": response.status_code if response else None,
        "response_body": response.json() if response and response.content else None,
        "error": str(error) if error else None
    }
    logger.info(json.dumps(log_entry, indent=2))

def api_request(method, url, json=None, headers=None):
    """Wrapper for requests to handle logging"""
    try:
        response = requests.request(method, url, json=json, headers=headers)
        log_request_response(method, url, json, response)
        return response
    except Exception as e:
        log_request_response(method, url, json, error=e)
        raise

# Test data generators
def generate_email():
    return f"test_{''.join(random.choices(string.ascii_lowercase, k=8))}@example.com"

def generate_phone():
    return f"+1{''.join(random.choices(string.digits, k=10))}"

def generate_login():
    return f"user_{''.join(random.choices(string.ascii_lowercase, k=8))}"

@pytest.fixture
def auth_headers():
    login_data = {
        "username": "admin",
        "password": "admin",
        "rememberMe": True
    }
    response = api_request("POST", f"{BASE_URL}/authenticate", json=login_data)
    token = response.json()["id_token"]
    return {"Authorization": f"Bearer {token}"}

class TestUserAPI:
    def setup_method(self):
        self.test_user_login = None

    @pytest.fixture
    def sample_admin_user_data(self):
        return {
            "login": generate_login(),
            "firstName": "Admin",
            "lastName": "User",
            "email": generate_email(),
            "activated": True,
            "langKey": "en",
            "authorities": ["ROLE_USER"]
        }

    def test_create_user(self, auth_headers, sample_admin_user_data):
        response = api_request("POST", f"{BASE_URL}/admin/users", json=sample_admin_user_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_user_login = response.json()["login"]

    def test_get_user(self, auth_headers, sample_admin_user_data):
        self.test_create_user(auth_headers, sample_admin_user_data)
        response = api_request("GET", f"{BASE_URL}/admin/users/{self.test_user_login}", headers=auth_headers)
        assert response.status_code == 200

    def test_delete_user(self, auth_headers, sample_admin_user_data):
        self.test_create_user(auth_headers, sample_admin_user_data)
        response = api_request("DELETE", f"{BASE_URL}/admin/users/{self.test_user_login}", headers=auth_headers)
        assert response.status_code == 204

class TestStaffAPI:
    def setup_method(self):
        self.test_staff_id = None

    @pytest.fixture
    def sample_staff_data(self):
        return {
            "firstName": "John",
            "lastName": "Doe",
            "email": generate_email(),
            "phone": generate_phone(),
            "role": "WAITER",
            "joinDate": datetime.now().strftime("%Y-%m-%d"),
            "isActive": True
        }

    def test_create_staff(self, auth_headers, sample_staff_data):
        response = api_request("POST", f"{BASE_URL}/staff", json=sample_staff_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_staff_id = response.json()["id"]

    def test_get_staff(self, auth_headers, sample_staff_data):
        self.test_create_staff(auth_headers, sample_staff_data)
        response = api_request("GET", f"{BASE_URL}/staff/{self.test_staff_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_staff(self, auth_headers, sample_staff_data):
        self.test_create_staff(auth_headers, sample_staff_data)
        updated_data = sample_staff_data.copy()
        updated_data["id"] = self.test_staff_id
        updated_data["firstName"] = "Jane"
        response = api_request("PUT", f"{BASE_URL}/staff/{self.test_staff_id}", json=updated_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["firstName"] == "Jane"

    def test_partial_update_staff(self, auth_headers, sample_staff_data):
        self.test_create_staff(auth_headers, sample_staff_data)
        patch_data = {
            "id": self.test_staff_id,
            "firstName": "Janet"
        }
        response = api_request("PATCH", f"{BASE_URL}/staff/{self.test_staff_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["firstName"] == "Janet"

    def test_delete_staff(self, auth_headers, sample_staff_data):
        self.test_create_staff(auth_headers, sample_staff_data)
        response = api_request("DELETE", f"{BASE_URL}/staff/{self.test_staff_id}", headers=auth_headers)
        assert response.status_code == 204

class TestCustomerAPI:
    def setup_method(self):
        self.test_customer_id = None

    @pytest.fixture
    def sample_customer_data(self):
        return {
            "firstName": "Alice",
            "lastName": "Smith",
            "email": generate_email(),
            "phone": generate_phone(),
            "addressLine1": "123 Main St",
            "city": "Boston",
            "country": "USA"
        }

    def test_create_customer(self, auth_headers, sample_customer_data):
        response = api_request("POST", f"{BASE_URL}/customers", json=sample_customer_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_customer_id = response.json()["id"]

    def test_get_customer(self, auth_headers, sample_customer_data):
        self.test_create_customer(auth_headers, sample_customer_data)
        response = api_request("GET", f"{BASE_URL}/customers/{self.test_customer_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_customer(self, auth_headers, sample_customer_data):
        self.test_create_customer(auth_headers, sample_customer_data)
        updated_data = sample_customer_data.copy()
        updated_data["id"] = self.test_customer_id
        updated_data["firstName"] = "Alicia"
        response = api_request("PUT", f"{BASE_URL}/customers/{self.test_customer_id}", json=updated_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["firstName"] == "Alicia"

    def test_partial_update_customer(self, auth_headers, sample_customer_data):
        self.test_create_customer(auth_headers, sample_customer_data)
        patch_data = {
            "id": self.test_customer_id,
            "firstName": "Alexandra"
        }
        response = api_request("PATCH", f"{BASE_URL}/customers/{self.test_customer_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["firstName"] == "Alexandra"

    def test_delete_customer(self, auth_headers, sample_customer_data):
        self.test_create_customer(auth_headers, sample_customer_data)
        response = api_request("DELETE", f"{BASE_URL}/customers/{self.test_customer_id}", headers=auth_headers)
        assert response.status_code == 204

class TestMenuItemAPI:
    def setup_method(self):
        self.test_menu_item_id = None

    @pytest.fixture
    def sample_menu_item_data(self):
        return {
            "name": "Spaghetti Carbonara",
            "description": "Classic Italian pasta dish",
            "price": 15.99,
            "category": "MAIN_COURSE",
            "spicyLevel": "NOT_SPICY",
            "isVegetarian": False,
            "isAvailable": True
        }

    def test_create_menu_item(self, auth_headers, sample_menu_item_data):
        response = api_request("POST", f"{BASE_URL}/menu-items", json=sample_menu_item_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_menu_item_id = response.json()["id"]

    def test_get_menu_item(self, auth_headers, sample_menu_item_data):
        self.test_create_menu_item(auth_headers, sample_menu_item_data)
        response = api_request("GET", f"{BASE_URL}/menu-items/{self.test_menu_item_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_menu_item(self, auth_headers, sample_menu_item_data):
        self.test_create_menu_item(auth_headers, sample_menu_item_data)
        updated_data = sample_menu_item_data.copy()
        updated_data["id"] = self.test_menu_item_id
        updated_data["price"] = 17.99
        response = api_request("PUT", f"{BASE_URL}/menu-items/{self.test_menu_item_id}", json=updated_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["price"] == 17.99

    def test_partial_update_menu_item(self, auth_headers, sample_menu_item_data):
        self.test_create_menu_item(auth_headers, sample_menu_item_data)
        patch_data = {
            "id": self.test_menu_item_id,
            "price": 16.99
        }
        response = api_request("PATCH", f"{BASE_URL}/menu-items/{self.test_menu_item_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["price"] == 16.99

    def test_delete_menu_item(self, auth_headers, sample_menu_item_data):
        self.test_create_menu_item(auth_headers, sample_menu_item_data)
        response = api_request("DELETE", f"{BASE_URL}/menu-items/{self.test_menu_item_id}", headers=auth_headers)
        assert response.status_code == 204

class TestOrderAPI:
    def setup_method(self):
        self.test_order_id = None

    @pytest.fixture
    def sample_order_data(self):
        return {
            "orderDate": datetime.now().strftime("%Y-%m-%dT%H:%M:00Z"),
            "status": "NEW",
            "totalAmount": 45.98,
            "paymentMethod": "CREDIT_CARD",
            "paymentReference": "REF123",
            "specialInstructions": "No onions please"
        }

    def test_create_order(self, auth_headers, sample_order_data):
        response = api_request("POST", f"{BASE_URL}/orders", json=sample_order_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_order_id = response.json()["id"]

    def test_get_order(self, auth_headers, sample_order_data):
        self.test_create_order(auth_headers, sample_order_data)
        response = api_request("GET", f"{BASE_URL}/orders/{self.test_order_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_order(self, auth_headers, sample_order_data):
        self.test_create_order(auth_headers, sample_order_data)
        updated_data = sample_order_data.copy()
        updated_data["id"] = self.test_order_id
        updated_data["status"] = "PREPARING"
        response = api_request("PUT", f"{BASE_URL}/orders/{self.test_order_id}", json=updated_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["status"] == "PREPARING"

    def test_partial_update_order(self, auth_headers, sample_order_data):
        self.test_create_order(auth_headers, sample_order_data)
        patch_data = {
            "id": self.test_order_id,
            "status": "READY"
        }
        response = api_request("PATCH", f"{BASE_URL}/orders/{self.test_order_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["status"] == "READY"

    def test_delete_order(self, auth_headers, sample_order_data):
        self.test_create_order(auth_headers, sample_order_data)
        response = api_request("DELETE", f"{BASE_URL}/orders/{self.test_order_id}", headers=auth_headers)
        assert response.status_code == 204

class TestOrderItemAPI:
    def setup_method(self):
        self.test_order_item_id = None
        self.test_menu_item_id = None
        self.test_order_id = None

    def create_prerequisites(self, auth_headers):
        # Create menu item
        menu_item_data = {
            "name": "Test Item",
            "price": 10.99,
            "category": "MAIN_COURSE",
            "isAvailable": True
        }
        menu_response = api_request("POST", f"{BASE_URL}/menu-items", json=menu_item_data, headers=auth_headers)
        self.test_menu_item_id = menu_response.json()["id"]

        # Create order
        order_data = {
            "orderDate": datetime.now().strftime("%Y-%m-%dT%H:%M:00Z"),
            "status": "NEW",
            "totalAmount": 10.99,
            "paymentMethod": "CASH"
        }
        order_response = api_request("POST", f"{BASE_URL}/orders", json=order_data, headers=auth_headers)
        self.test_order_id = order_response.json()["id"]

    @pytest.fixture
    def sample_order_item_data(self, auth_headers):
        self.create_prerequisites(auth_headers)
        return {
            "quantity": 1,
            "notes": "Extra cheese",
            "subtotal": 10.99,
            "menuItem": {"id": self.test_menu_item_id},
            "order": {"id": self.test_order_id}
        }

    def test_create_order_item(self, auth_headers, sample_order_item_data):
        response = api_request("POST", f"{BASE_URL}/order-items", json=sample_order_item_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_order_item_id = response.json()["id"]

    def test_get_order_item(self, auth_headers, sample_order_item_data):
        self.test_create_order_item(auth_headers, sample_order_item_data)
        response = api_request("GET", f"{BASE_URL}/order-items/{self.test_order_item_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_order_item(self, auth_headers, sample_order_item_data):
        self.test_create_order_item(auth_headers, sample_order_item_data)
        updated_data = sample_order_item_data.copy()
        updated_data["id"] = self.test_order_item_id
        updated_data["quantity"] = 2
        updated_data["subtotal"] = 21.98
        
        response = api_request(
            "PUT",
            f"{BASE_URL}/order-items/{self.test_order_item_id}", 
            json=updated_data,
            headers=auth_headers
        )
        
        assert response.status_code == 200
        assert response.json()["quantity"] == 2
        assert response.json()["subtotal"] == 21.98

    def test_partial_update_order_item(self, auth_headers, sample_order_item_data):
        self.test_create_order_item(auth_headers, sample_order_item_data)
        patch_data = {
            "id": self.test_order_item_id,
            "quantity": 3,
            "subtotal": 32.97
        }
        response = api_request("PATCH", f"{BASE_URL}/order-items/{self.test_order_item_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["quantity"] == 3
        assert response.json()["subtotal"] == 32.97

    def test_delete_order_item(self, auth_headers, sample_order_item_data):
        self.test_create_order_item(auth_headers, sample_order_item_data)
        response = api_request("DELETE", f"{BASE_URL}/order-items/{self.test_order_item_id}", headers=auth_headers)
        assert response.status_code == 204

class TestReservationAPI:
    def setup_method(self):
        self.test_reservation_id = None

    @pytest.fixture
    def sample_reservation_data(self):
        return {
            "reservationDate": (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%dT%H:%M:00Z"),
            "partySize": 4,
            "status": "PENDING",
            "customerName": "Bob Johnson",
            "customerEmail": generate_email(),
            "customerPhone": generate_phone(),
            "specialRequests": "Window seat preferred"
        }

    def test_create_reservation(self, auth_headers, sample_reservation_data):
        response = api_request("POST", f"{BASE_URL}/reservations", json=sample_reservation_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_reservation_id = response.json()["id"]

    def test_get_reservation(self, auth_headers, sample_reservation_data):
        self.test_create_reservation(auth_headers, sample_reservation_data)
        response = api_request("GET", f"{BASE_URL}/reservations/{self.test_reservation_id}", headers=auth_headers)
        assert response.status_code == 200

    def test_update_reservation(self, auth_headers, sample_reservation_data):
        self.test_create_reservation(auth_headers, sample_reservation_data)
        updated_data = sample_reservation_data.copy()
        updated_data["id"] = self.test_reservation_id
        updated_data["status"] = "CONFIRMED"
        response = api_request("PUT", f"{BASE_URL}/reservations/{self.test_reservation_id}", json=updated_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["status"] == "CONFIRMED"

    def test_partial_update_reservation(self, auth_headers, sample_reservation_data):
        self.test_create_reservation(auth_headers, sample_reservation_data)
        patch_data = {
            "id": self.test_reservation_id,
            "status": "CANCELLED"
        }
        response = api_request("PATCH", f"{BASE_URL}/reservations/{self.test_reservation_id}", json=patch_data, headers=auth_headers)
        assert response.status_code == 200
        assert response.json()["status"] == "CANCELLED"

    def test_delete_reservation(self, auth_headers, sample_reservation_data):
        self.test_create_reservation(auth_headers, sample_reservation_data)
        response = api_request("DELETE", f"{BASE_URL}/reservations/{self.test_reservation_id}", headers=auth_headers)
        assert response.status_code == 204

class TestAuthorityAPI:
    def setup_method(self):
        self.test_authority_name = None

    @pytest.fixture
    def sample_authority_data(self):
        return {
            "name": f"ROLE_TEST_{random.randint(1000, 9999)}"
        }

    def test_create_authority(self, auth_headers, sample_authority_data):
        response = api_request("POST", f"{BASE_URL}/authorities", json=sample_authority_data, headers=auth_headers)
        assert response.status_code == 201
        self.test_authority_name = response.json()["name"]

    def test_get_all_authorities(self, auth_headers):
        response = api_request("GET", f"{BASE_URL}/authorities", headers=auth_headers)
        assert response.status_code == 200
        assert isinstance(response.json(), list)

    def test_get_authority(self, auth_headers, sample_authority_data):
        self.test_create_authority(auth_headers, sample_authority_data)
        response = api_request("GET", f"{BASE_URL}/authorities/{self.test_authority_name}", headers=auth_headers)
        assert response.status_code == 200

    def test_delete_authority(self, auth_headers, sample_authority_data):
        self.test_create_authority(auth_headers, sample_authority_data)
        response = api_request("DELETE", f"{BASE_URL}/authorities/{self.test_authority_name}", headers=auth_headers)
        assert response.status_code == 204

if __name__ == "__main__":
    pytest.main([__file__, "-v"])