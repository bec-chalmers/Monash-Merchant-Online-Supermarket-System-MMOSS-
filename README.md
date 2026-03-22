# Monash Merchant Online Supermarket System (MMOSS) - README
## Overview

This Java console application is a terminal-based application that serves as a mockup of the Monash Merchant system
Register. It comprises the following high level features: 
1. Log in
2. Browse and Filter Products 
3. Shopping and Cart 
4. Checkout and Order Confirmation
5. VIP Membership 
6. Order and Membership History View

The program is designed to run directly in an IDE (i.e. IntelliJ IDEA) using only standard Java libraries — no external dependencies required. No database setup is required — all operations are file-based for easy portability.
## Installation Guidelines
### Software Requirements 
| Requirement                    | Version / Notes                         |
| ------------------------------ |-----------------------------------------|
| **Java** | Version **23.x** or higher              |
| **IDE**                        | IntelliJ IDEA *(recommended)* |
| **Encoding**                   | UTF-8                                   |
| **Libraries**                  | None — only uses standard Java packages |

### Data Files
The system stores and reads customer, order, and VIP membership data in the **/data** directory.
The folder structure should look like this:
![Data Folder Structure](images/Data.png)

### Project Setup Instructions 
**Step 1: Open the Project**
1. Launch IntelliJ IDEA 
2. Go to File → Open… 
3. Select your project folder and click OK

**Step 2: Configure Project SDK**
1. Go to File → Project Structure → Project SDK
2. Choose JDK 17 (or your installed version)
3. Click Apply → OK

**Step 3: Check Folder Structure**

It should look like this:
![Folder Structure](images/Folder path.png)

**Step 4: Run the Application**
1. In Project Explorer, locate MMOSSApp.java
2. Right-click it → Select Run 'MMOSSApp.java' 
3. The console window will display the main menu.
4. Follow console instructions to login, shop, or manage membership

**Example output:**

![Example Starting output](images/Screen.png)

### Troubleshooting 
| **Issue**                        | **Possible Cause**        | **Solution**                                                       |
| -------------------------------- | ------------------------- | ------------------------------------------------------------------ |
| Program fails to compile         | JDK not configured        | Go to **File → Project Structure → SDK → Set to JDK 17**           |

