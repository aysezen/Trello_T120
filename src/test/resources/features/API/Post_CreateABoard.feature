@api
Feature: Create a new board.
  
  Scenario: 
    
    Given Api "1/boards" Path Parametreleri set edilir
    Then Api "name=Sari&key=7e84e42fad3d8e58a93c9f657888312b&token=ATTA0fc0df951c83830f1db0786c4d1767abfeb70a1805f59ffc49b177d76b410d908B655241" Query Parametreleri set edilir
    Then Post request gonderilir
    Then Donen response body'den idBoard kaydedilir.
    Then Donen Response'in status code'unun 200 oldugu dogrulanir





