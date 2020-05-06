# Warehouse Manager

## Manage the sales and storage of your business

The application will allow **business owners** to manage and keep track of the products that they are selling. Features of the application include:

- Status of products (in stock, out of stock)
- Selling price of each product
- Number of units sold and the total revenue earned

The people who use this application will be owners of small online businesses who want a platform to manage the products they are selling. 
This project is interesting to me because in the future, I would also like to start my own online selling business and would need an application like this to manage my business.

## User Stories
-	Add a product to list of selling products 
-	Remove products from list of selling products
-   Filter products by certain characteristics
-   Arrange products by certain characteristics
-   Edit and change products
-   Display the list of pending orders
-   Add, resolve or remove pending orders
-   Save a warehouse
-   Choose a warehouse to load

##Instructions for Grader
-   When you first run the main method, a frame will pop up prompting you on what warehouse you want to load. This is how the state of the application can be reloaded.
-   You can add and remove a product from the list of products by clicking the "Add or Remove Product" Button:
   -   To add a product, click on "Add row to table" and Manually type in the details of the product you want to add.
   -   To remove a product, select the corresponding rows and click the "remove selected products" Button.
-   You can locate the visual component of the application by clicking on the "Graph Products" Button on the home panel (click on the "Home" button if you're on a different panel)
   -   Note that you can select what type of data to display from the JComboBox at the top, and you can switch between pie charts and bar graphs using the tabs at the top left corner
-   You can save the state of my application by clicking the save button, present on many different panels.
-   You can load the state of my application by returning to the original frame that popped up when you launched the application and loading the warehouse again

-   Cool side notes:
   - The TechWarehouse is the warehouse with the most products in it, so I'd load that. Other warehouses have the same functionality though.
   - You can pull up the graphs for different warehouses and compare them side by side
   - There's a button that says "Show Pending Orders". Clicking on it will bring up another frame of that warehouse's orders that have yet to be processed and shipped. I thought it would be a cool functionality to add.
   - These orders can be resolved by clicking the "Resolve Pending Order" button (which will update the main warehouse after you save the changes).
   - There's also a search bar of sorts at the top of the application, you can use it to search/filter through the warehouse. Have fun.  
   
## Phase 4
Phase 4 Task 2:

-   Option chosen: Test and design a robust class
-   All three classes in my models folder make use of robust designs (many of their methods throw exceptions). Model tests in the models folder also fully test all exception handling behavior.
-   To make it easier for TAs: look at the ProductList class. The first few robust methods appear immediately after the constructor
(there are also more robust methods further down in the class). The corresponding tests for these methods and exceptions can be found in
the test folder, in the ProductListTest class (they are the first few methods tested).

Phase 4 Task 3:

-   Problem 1: The GUI class was handling functionality for multiple different JFrames (the warehouse itself, the graphs, the error windows), 
etc. Cohesion was very low. 
-   Problem 2: GUI class still had to deal with the functionality of the JFrame related to Pending Orders (due to how closely related they are).
Cohesion was still very low.
-   Problem 3: Too much coupling between ProductList, GUI and PendingOrders Frame. PendingOrdersFrame holds an instance of both GUI and ProductList,
while GUI already holds an instance of ProductList.
-   Problem 4: The classes ProductList and ProductEntry had many methods similar to each other that served the same purpose, and so each 
class's cohesion was low (The code was very confusing for sure, many repetitive methods).
-   Problem 5: ProductList class had to deal with pre-processing the data of the warehouse before it could be stored, and post-processing
the data after it was loaded. This decreased cohesion inside the class.
-   Problem 6: ProductList manages both a list of ProductEntry objects and a list of PendingOrder objects. (I'm hesitant to include this as a
problem though, since both lists are quite related to each other. I can argue both for and against them being in the same class. I'll put it
as a (unsolved) problem anyway).

- Improvements made to above problems:

- Improvements for problem 1: Separated the original GUI class into multiple smaller classes. Ensured that each class only managed its own 
JFrame, and that each class (and its JFrame) had a singular focus. The GUI class now only has 2 "focuses" and JFrames: the JFrame for the warehouse itself,
and the JFrame for Pending Orders (which could not be refactored into its own class at the time). GraphFrame was made and deals exclusively with
graphing the data of the warehouse. ErrorWindow was made and deals exclusively with displaying errors. In addition, While separating the big class into
multiple smaller classes, I made sure that the classes didn't hold instances of each other, but constructed new objects of each other when called.
For example,  GUI does not have an instance of GraphFrame; when the user wants to display the graphs of his warehouse, an entire new GraphFrame is created.
This kept coupling between GUI classes to a minimum.

- Improvements for problem 2: Found a way for a GUI object to pass an instance of itself as parameter when calling PendingOrdersFrame. This
allowed me to refactor PendingOrdersFrame into its own class without losing any of its functionality, something that wasn't possible when I
was solving problem 2. Now, PendingOrdersFrame deals exclusively with managing Pending Orders of the warehouse and displaying its data in a 
JFrame; and GUI deals exclusively with the products of the warehouse and displaying its data as a JFrame. All classes in my UI package now only
manage a single JFrame each, and each class is specifically focused on one task, which improved their cohesion.

- Improvements for problem 3: removed all instances of warehouse from PendingOrdersFrame. Whenever PendingOrdersFrame needed to access the warehouse,
it used accessor methods from GUI instead. This greatly reduced coupling. 

- Improvements for problem 4: Refactored code so that all methods dealing with the entire list of products in the warehouse  (e.g filtering) 
lied strictly inside ProductList, while methods dealing with editing a single product (e.g changing its price) lied strictly inside
ProductEntry. Added "getter/indexing" methods to obtain particular ProductEntry objects from ProductList so that if a change for every product was required,
These indexing methods could be used to access every single product instead of ProductList directly accessing it itself. This greatly increased
cohesion.

- Improvements for problem 5: Moved all code relating to saving to JsonWriter class and all code related to loading to JsonReader class. In 
addition, made the methods of JsonWriter and JsonReader static, so that no instance of them need to exist. ProductList can now use these classes
like a black box to save and load files; ProductList does nothing related to saving/loading anymore. This improved cohesion.

