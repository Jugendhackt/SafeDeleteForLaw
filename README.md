[![Build status](https://dev.azure.com/TheMinefighter/SafeDeleteForLaw/_apis/build/status/SafeDeleteForLaw-.NET%20Desktop-CI%20(1))](https://dev.azure.com/TheMinefighter/SafeDeleteForLaw/_build/latest?definitionId=9)
# SafeDeleteForLaw
This projects aim is it to provide a way to check if one law is referenced somewhere else before it get's deleted.
It uses german law info from the government site ([gesetze-im-internet.de](gesetze-im-internet.de)).

It is divided in:
 - a frontend (javafx, obsolete)
 - a new Frontend (WPF)
 - two backends (both c#):
   - One for crawling the laws in xml form
   - one to detect references
 - one json file for data exchange ([spec](./ExchangeJsonSpec.md))
