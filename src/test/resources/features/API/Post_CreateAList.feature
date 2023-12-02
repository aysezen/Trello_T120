@api
Feature: Create a new List on a board

  Scenario:

    Given Api "1/lists" Path Parametreleri set edilir
    Then Api "name=Eksikler&key=7e84e42fad3d8e58a93c9f657888312b&token=ATTA0fc0df951c83830f1db0786c4d1767abfeb70a1805f59ffc49b177d76b410d908B655241" Query Parametreleri set edilir
    Then Create a new List Api icin idBoard query parametresi hazirlanir
    Then Post request gonderilir
    Then Donen Response'in status code'unun 200 oldugu dogrulanir