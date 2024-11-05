# Byte-Me-Food-App
"Byte Me!" is a CLI-based food ordering system designed for college canteens, enabling students to browse menus, place orders, and track deliveries from their hostel rooms. The system also allows canteen staff to manage menu items and process orders efficiently, while maintaining order histories for easy access.

This document provides an overview of the classes, exceptions, and interfaces used in the OSystem. The system allows users to manage their orders efficiently, handle menu items, and provide a seamless shopping experience.

## Classes

1. **User**: Represents a user with attributes for username, password, and login status. It has a reference to an `Order_Manager` for managing orders.

2. **User_Manager**: Manages a collection of `User` objects using a `Set<User>`, which prevents duplicate users and allows for quick checks on whether a user exists.

3. **Order_Manager**: Handles orders using a `TreeMap<Cart, UUID>` for both VIP and regular orders, sorted by checkout time. It also keeps track of completed and denied orders with a `Map<UUID, Cart>` for easy order tracking.

4. **Menu**: Maintains a menu of items in a `TreeMap<Item, String>`, which sorts items by price and ensures that item names are unique. It provides functions for adding, removing, and searching for items.

5. **Item Class**: Represents a menu item with properties like name, price, category, and availability. It includes getters and setters for these properties, along with a `toString` method for easy representation.

6. **Customer Class**: Inherits from `User` and manages a `TreeMap` of `Cart` objects and their corresponding order IDs as an order history. It also has methods for logging in, logging out, viewing order history, and converting to a VIP customer.

7. **Cart Class**: Implements `CartInterface` and holds the items ordered by the customer along with their quantities, status, total amount, and a description. The class offers methods for adding and removing items, checking out, and managing payments.

## Exceptions

1. **ChangeWithoutLogin**: Thrown when a user tries to make changes without being logged in.

2. **IllegalLoginException**: Used when a login attempt fails due to invalid credentials.

3. **InvalidSwitchChoice**: Raised when a user selects an option that isn’t valid in a switch-case statement.

4. **ItemDoesntExistException**: Thrown when an operation is attempted on an item that doesn’t exist in the system.

5. **NameAlreadyUsedException**: Used when trying to create a new user or item with a name that is already taken.

## Interfaces

1. **CartInterface**: Defines methods for managing items in a shopping cart, including adding, removing, and checking out.

2. **MenuManager**: Specifies methods for managing menu items, including adding, removing, searching, and filtering items.

---
