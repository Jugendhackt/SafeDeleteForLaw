[![Build status](https://dev.azure.com/TheMinefighter/SafeDeleteForLaw/_apis/build/status/SafeDeleteForLaw-.NET%20Desktop-CI)](https://dev.azure.com/TheMinefighter/SafeDeleteForLaw/_build/latest?definitionId=7)
# SafeDeleteForLaw
This projects aim is it to provide a way to check if one law is referenced somewhere else before it get's deleted.
It uses german law info from the government site ([gesetze-im-internet.de](gesetze-im-internet.de)).

It is divided in:
 - a frontend (javafx)
 - two backend2 (both c#, netcoreapp 2.2):
   - One for crawling the laws in xml form (takes depending on your internet connection about one hour) 
   - one to detect references (takes depending on your hardware around one minute).
 - one json file for data exchange ([spec](./ExchangeJsonSpec.md))
