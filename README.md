# QR Guard by Digital Dreamweavers
QR Guard is a simple and easy to use quishing (QR Code Phishing) detector. 
It has a simple interface: Just point and scan, and you'll get a safety rating either 
from our Firestore or CISCO's PhishTank. 

## How it works
QR Guard uses a mutual benefit system. 
We give you an app that tells you whether QR codes are safe or not, 
you give us analytics data to help us understand where and how prevalent quishing is. 

## Data QR Guard collects and stores as of 28th May 2024.
In our Quishing Firestore: 
- URLs in both Base64 and plain text. 
- How many times this URL has been scanned.
- The amount of negative and positive ratings. 
- The time and date of the scan. 
- Locations where this URL was scanned.
We believe this data is essential to understanding patterns in quishing incidents.

In our Users Firestore: 
- Your email.
We get your display name on the fly from Google. 

Firebase Analytics:
- Everytime an unsafe QR code is scanned, regardless of whether a user submits a rating or not.
Data in Firebase Analytics is anonymously collected.