#maxint=2000.
#const n=4.
#const size=500.
holds(on(b0,1,2),0).
holds(on(b1,1,1),0).
-holds(F_G,I_G):-not  holds(F_G,I_G),fluent(F_G),step(I_G).
holds(on(B_G,R_G,C_G),I_G):-occurs(put(B_G,R_G,C_G),VAR_0),VAR_0=I_G-1,step(VAR_0),step(I_G),block(B_G),co(R_G),co(C_G).
-holds(on(B_G,R_G,C_G),I_G):-holds(on(B_G,R1_G,C1_G),I_G),R_G!=R1_G,step(I_G),co(R1_G),block(B_G),co(C1_G),co(C_G),co(R_G).
-holds(on(B_G,R_G,C_G),I_G):-holds(on(B_G,R1_G,C1_G),I_G),C_G!=C1_G,block(B_G),co(C1_G),step(I_G),co(R1_G),co(C_G),co(R_G).
holds(F_G,I_G):-holds(F_G,VAR_0),not  -holds(F_G,I_G),VAR_0=I_G-1,fluent(F_G),step(VAR_0),step(I_G).
-holds(F_G,I_G):--holds(F_G,VAR_0),not  holds(F_G,I_G),VAR_0=I_G-1,fluent(F_G),step(I_G),step(VAR_0).
-occurs(put(B_G,R1_G,C1_G),I_G):-holds(on(B_G,R_G,C_G),I_G),holds(on(B1_G,VAR_0,C_G),I_G),VAR_0=R_G+1,co(VAR_0),co(C_G),block(B_G),co(R1_G),step(I_G),co(R_G),co(C1_G),block(B1_G).
-occurs(put(B_G,R_G,C_G),I_G):-holds(on(B1_G,R_G,C_G),I_G),co(R_G),co(C_G),step(I_G),block(B_G),block(B1_G).
-occurs(put(B_G,R_G,C_G),I_G):--holds(on(B1_G,VAR_0,C_G),I_G),C_G>1,B_G!=B1_G,VAR_0=R_G-1,step(I_G),co(VAR_0),co(C_G),block(B_G),block(B1_G),co(R_G).
goal(I_G):-holds(on(b1,2,3),I_G),step(I_G).
success(true):-goal(I_G),step(I_G).
:-not  success(true).
occurs(A_G,I_G)|-occurs(A_G,I_G):-not  goal(I_G),action(A_G),step(I_G).
:-occurs(A_G,I_G),occurs(A1_G,I_G),A_G!=A1_G,step(I_G),action(A_G),action(A1_G).
frame(1..60).
animate(line_color(b0,green),F_G):-frame_(F_G).
animate(line_color(b1,red),F_G):-frame_(F_G).
animate(draw_line(B_G,VAR_0,VAR_3,VAR_6,VAR_11),VAR_16):-holds(on(B_G,R1_G,C1_G),I_G),occurs(put(B_G,R2_G,C2_G),I_G),frame(F_G),R_G=VAR_20,C_G=VAR_24,nat(C_G),nat(R_G),VAR_1=4+1,VAR_2=500/VAR_1,VAR_0=VAR_2*C_G,VAR_4=4+1,VAR_5=500/VAR_4,VAR_3=VAR_5*R_G,VAR_7=4+1,VAR_8=500/VAR_7,VAR_6=VAR_8*C_G,VAR_9=4+1,VAR_10=500/VAR_9,VAR_13=4+1,VAR_14=500/VAR_13,VAR_12=VAR_14*R_G,VAR_11=VAR_12+VAR_10,VAR_15=60*I_G,VAR_16=F_G+VAR_15,VAR_19=R2_G-R1_G,VAR_18=VAR_19/60,VAR_17=VAR_18*F_G,VAR_20=R1_G+VAR_17,VAR_23=C2_G-C1_G,VAR_22=VAR_23/60,VAR_21=VAR_22*F_G,VAR_24=C1_G+VAR_21,col(VAR_6),co(R1_G),co(C2_G),block(B_G),frame_(F_G),col(VAR_0),co(R2_G),step(I_G),row(VAR_11),row(VAR_3),frame_(VAR_16),co(C1_G).
animate(draw_line(B_G,VAR_0,VAR_5,VAR_11,VAR_17),I_G):-holds(on(B_G,R1_G,C1_G),I_G),occurs(put(B_G,R2_G,C2_G),I_G),frame(F_G),R_G=VAR_24,C_G=VAR_28,nat(C_G),nat(R_G),VAR_1=4+1,VAR_2=500/VAR_1,VAR_0=VAR_2*C_G,VAR_3=4+1,VAR_4=500/VAR_3,VAR_7=4+1,VAR_8=500/VAR_7,VAR_6=VAR_8*R_G,VAR_5=VAR_6+VAR_4,VAR_9=4+1,VAR_10=500/VAR_9,VAR_13=4+1,VAR_14=500/VAR_13,VAR_12=VAR_14*C_G,VAR_11=VAR_12+VAR_10,VAR_15=4+1,VAR_16=500/VAR_15,VAR_19=4+1,VAR_20=500/VAR_19,VAR_18=VAR_20*R_G,VAR_17=VAR_18+VAR_16,VAR_23=R2_G-R1_G,VAR_22=VAR_23/60,VAR_21=VAR_22*F_G,VAR_24=R1_G+VAR_21,VAR_27=C2_G-C1_G,VAR_26=VAR_27/60,VAR_25=VAR_26*F_G,VAR_28=C1_G+VAR_25,co(R2_G),step(I_G),block(B_G),row(VAR_5),co(C2_G),row(VAR_17),col(VAR_11),co(R1_G),frame_(F_G),frame_(I_G),col(VAR_0),co(C1_G).
animate(draw_line(B_G,VAR_2,VAR_8,VAR_14,VAR_18),I_G):-holds(on(B_G,R1_G,C1_G),I_G),occurs(put(B_G,R2_G,C2_G),I_G),frame(F_G),R_G=VAR_24,C_G=VAR_28,nat(C_G),nat(R_G),VAR_0=4+1,VAR_1=500/VAR_0,VAR_4=4+1,VAR_5=500/VAR_4,VAR_3=VAR_5*C_G,VAR_2=VAR_3+VAR_1,VAR_6=4+1,VAR_7=500/VAR_6,VAR_10=4+1,VAR_11=500/VAR_10,VAR_9=VAR_11*R_G,VAR_8=VAR_9+VAR_7,VAR_12=4+1,VAR_13=500/VAR_12,VAR_16=4+1,VAR_17=500/VAR_16,VAR_15=VAR_17*C_G,VAR_14=VAR_15+VAR_13,VAR_19=4+1,VAR_20=500/VAR_19,VAR_18=VAR_20*R_G,VAR_23=R2_G-R1_G,VAR_22=VAR_23/60,VAR_21=VAR_22*F_G,VAR_24=R1_G+VAR_21,VAR_27=C2_G-C1_G,VAR_26=VAR_27/60,VAR_25=VAR_26*F_G,VAR_28=C1_G+VAR_25,row(VAR_8),co(R2_G),co(R1_G),step(I_G),block(B_G),row(VAR_18),co(C2_G),col(VAR_14),co(C1_G),col(VAR_2),frame_(I_G),frame_(F_G).
animate(draw_line(B_G,VAR_2,VAR_6,VAR_9,VAR_12),I_G):-holds(on(B_G,R1_G,C1_G),I_G),occurs(put(B_G,R2_G,C2_G),I_G),frame(F_G),R_G=VAR_18,C_G=VAR_22,nat(C_G),nat(R_G),VAR_0=4+1,VAR_1=500/VAR_0,VAR_4=4+1,VAR_5=500/VAR_4,VAR_3=VAR_5*C_G,VAR_2=VAR_3+VAR_1,VAR_7=4+1,VAR_8=500/VAR_7,VAR_6=VAR_8*R_G,VAR_10=4+1,VAR_11=500/VAR_10,VAR_9=VAR_11*C_G,VAR_13=4+1,VAR_14=500/VAR_13,VAR_12=VAR_14*R_G,VAR_17=R2_G-R1_G,VAR_16=VAR_17/60,VAR_15=VAR_16*F_G,VAR_18=R1_G+VAR_15,VAR_21=C2_G-C1_G,VAR_20=VAR_21/60,VAR_19=VAR_20*F_G,VAR_22=C1_G+VAR_19,col(VAR_9),step(I_G),col(VAR_2),co(C2_G),block(B_G),co(R2_G),frame_(I_G),row(VAR_6),co(C1_G),row(VAR_12),co(R1_G),frame_(F_G).
fluent(on(b1,4,2)).
fluent(on(b1,2,4)).
fluent(on(b1,3,3)).
fluent(on(b1,4,4)).
fluent(on(b1,1,3)).
fluent(on(b1,1,1)).
fluent(on(b1,2,2)).
fluent(on(b1,3,1)).
fluent(on(b0,1,4)).
fluent(on(b0,2,3)).
fluent(on(b0,3,2)).
fluent(on(b0,1,2)).
fluent(on(b0,2,1)).
fluent(on(b0,4,1)).
fluent(on(b0,4,3)).
fluent(on(b0,3,4)).
fluent(on(b1,3,4)).
fluent(on(b1,4,3)).
fluent(on(b1,2,1)).
fluent(on(b0,1,1)).
fluent(on(b1,1,4)).
fluent(on(b1,2,3)).
fluent(on(b1,3,2)).
fluent(on(b1,1,2)).
fluent(on(b1,4,1)).
fluent(on(b0,2,4)).
fluent(on(b0,3,3)).
fluent(on(b0,4,2)).
fluent(on(b0,1,3)).
fluent(on(b0,2,2)).
fluent(on(b0,3,1)).
fluent(on(b0,4,4)).
step(0).
step(1).
step(2).
block(b0).
block(b1).
co(1).
co(2).
co(3).
co(4).
action(put(b1,2,4)).
action(put(b1,3,1)).
action(put(b1,1,1)).
action(put(b1,4,4)).
action(put(b1,1,3)).
action(put(b1,2,2)).
action(put(b1,3,3)).
action(put(b1,4,2)).
action(put(b0,1,2)).
action(put(b0,2,1)).
action(put(b0,3,2)).
action(put(b0,4,1)).
action(put(b0,1,4)).
action(put(b0,2,3)).
action(put(b0,3,4)).
action(put(b0,4,3)).
action(put(b1,3,2)).
action(put(b1,4,1)).
action(put(b1,1,2)).
action(put(b1,2,1)).
action(put(b0,1,1)).
action(put(b1,3,4)).
action(put(b1,4,3)).
action(put(b1,1,4)).
action(put(b1,2,3)).
action(put(b0,1,3)).
action(put(b0,2,2)).
action(put(b0,3,1)).
action(put(b0,2,4)).
action(put(b0,3,3)).
action(put(b0,4,2)).
action(put(b0,4,4)).
frame_(88).
frame_(89).
frame_(110).
frame_(111).
frame_(112).
frame_(113).
frame_(114).
frame_(115).
frame_(116).
frame_(90).
frame_(117).
frame_(91).
frame_(118).
frame_(92).
frame_(119).
frame_(93).
frame_(94).
frame_(95).
frame_(96).
frame_(97).
frame_(10).
frame_(98).
frame_(11).
frame_(99).
frame_(12).
frame_(13).
frame_(14).
frame_(15).
frame_(16).
frame_(17).
frame_(18).
frame_(19).
frame_(120).
frame_(121).
frame_(1).
frame_(122).
frame_(2).
frame_(123).
frame_(3).
frame_(124).
frame_(4).
frame_(125).
frame_(5).
frame_(126).
frame_(6).
frame_(127).
frame_(7).
frame_(128).
frame_(8).
frame_(129).
frame_(9).
frame_(20).
frame_(21).
frame_(22).
frame_(23).
frame_(24).
frame_(25).
frame_(26).
frame_(27).
frame_(28).
frame_(29).
frame_(130).
frame_(131).
frame_(132).
frame_(133).
frame_(134).
frame_(135).
frame_(136).
frame_(137).
frame_(138).
frame_(139).
frame_(30).
frame_(31).
frame_(32).
frame_(33).
frame_(34).
frame_(35).
frame_(36).
frame_(37).
frame_(38).
frame_(39).
frame_(140).
frame_(141).
frame_(142).
frame_(143).
frame_(144).
frame_(145).
frame_(146).
frame_(147).
frame_(148).
frame_(149).
frame_(40).
frame_(41).
frame_(42).
frame_(43).
frame_(44).
frame_(45).
frame_(46).
frame_(47).
frame_(48).
frame_(49).
frame_(150).
frame_(151).
frame_(152).
frame_(153).
frame_(154).
frame_(155).
frame_(156).
frame_(157).
frame_(158).
frame_(159).
frame_(50).
frame_(51).
frame_(52).
frame_(53).
frame_(54).
frame_(55).
frame_(56).
frame_(57).
frame_(58).
frame_(59).
frame_(160).
frame_(161).
frame_(162).
frame_(163).
frame_(164).
frame_(165).
frame_(166).
frame_(167).
frame_(168).
frame_(169).
frame_(60).
frame_(61).
frame_(62).
frame_(63).
frame_(64).
frame_(65).
frame_(66).
frame_(67).
frame_(68).
frame_(69).
frame_(170).
frame_(171).
frame_(172).
frame_(173).
frame_(174).
frame_(175).
frame_(176).
frame_(177).
frame_(178).
frame_(179).
frame_(70).
frame_(71).
frame_(72).
frame_(73).
frame_(74).
frame_(75).
frame_(76).
frame_(77).
frame_(78).
frame_(79).
frame_(180).
frame_(100).
frame_(101).
frame_(102).
frame_(103).
frame_(104).
frame_(105).
frame_(106).
frame_(80).
frame_(107).
frame_(81).
frame_(108).
frame_(82).
frame_(109).
frame_(83).
frame_(84).
frame_(85).
frame_(86).
frame_(87).
nat(1462).
nat(1461).
nat(1460).
nat(1459).
nat(1458).
nat(1457).
nat(1456).
nat(1455).
nat(1454).
nat(1453).
nat(1452).
nat(1473).
nat(1472).
nat(1471).
nat(1470).
nat(0).
nat(1).
nat(1469).
nat(2).
nat(1468).
nat(3).
nat(1467).
nat(4).
nat(1466).
nat(5).
nat(1465).
nat(6).
nat(1464).
nat(7).
nat(1463).
nat(8).
nat(9).
nat(1000).
nat(1484).
nat(1483).
nat(1482).
nat(1481).
nat(1480).
nat(1479).
nat(1478).
nat(1477).
nat(1476).
nat(1475).
nat(1474).
nat(1011).
nat(1495).
nat(1010).
nat(1494).
nat(1493).
nat(1492).
nat(1491).
nat(1490).
nat(1008).
nat(1007).
nat(1006).
nat(1005).
nat(1489).
nat(1004).
nat(1488).
nat(1003).
nat(1487).
nat(1002).
nat(1486).
nat(1001).
nat(1485).
nat(1009).
nat(1022).
nat(1021).
nat(1020).
nat(1019).
nat(1018).
nat(1017).
nat(1016).
nat(1015).
nat(1499).
nat(1014).
nat(1498).
nat(1013).
nat(1497).
nat(1012).
nat(1496).
nat(1033).
nat(1032).
nat(1031).
nat(1030).
nat(1029).
nat(1028).
nat(1027).
nat(1026).
nat(1025).
nat(600).
nat(1024).
nat(601).
nat(1023).
nat(602).
nat(603).
nat(604).
nat(605).
nat(606).
nat(607).
nat(608).
nat(609).
nat(1044).
nat(1043).
nat(1042).
nat(1041).
nat(1040).
nat(1039).
nat(1038).
nat(1037).
nat(610).
nat(1036).
nat(611).
nat(1035).
nat(612).
nat(1034).
nat(613).
nat(614).
nat(615).
nat(616).
nat(617).
nat(618).
nat(619).
nat(1055).
nat(1054).
nat(1053).
nat(1052).
nat(1051).
nat(1050).
nat(1049).
nat(620).
nat(1048).
nat(621).
nat(1047).
nat(622).
nat(1046).
nat(623).
nat(1045).
nat(624).
nat(625).
nat(626).
nat(627).
nat(628).
nat(629).
nat(1066).
nat(1065).
nat(1064).
nat(1063).
nat(1062).
nat(1061).
nat(1060).
nat(630).
nat(631).
nat(1059).
nat(632).
nat(1058).
nat(633).
nat(1057).
nat(634).
nat(1056).
nat(635).
nat(636).
nat(637).
nat(638).
nat(639).
nat(1077).
nat(1076).
nat(1075).
nat(1074).
nat(1073).
nat(1072).
nat(1071).
nat(1070).
nat(640).
nat(641).
nat(642).
nat(643).
nat(1069).
nat(644).
nat(1068).
nat(645).
nat(1067).
nat(646).
nat(647).
nat(648).
nat(649).
nat(1080).
nat(1088).
nat(1087).
nat(1086).
nat(1085).
nat(1084).
nat(1083).
nat(1082).
nat(1081).
nat(650).
nat(651).
nat(652).
nat(653).
nat(654).
nat(655).
nat(1079).
nat(656).
nat(1078).
nat(657).
nat(658).
nat(659).
nat(1091).
nat(1090).
nat(1099).
nat(1098).
nat(1097).
nat(1096).
nat(1095).
nat(1094).
nat(1093).
nat(1092).
nat(660).
nat(661).
nat(662).
nat(663).
nat(664).
nat(665).
nat(666).
nat(667).
nat(1089).
nat(668).
nat(669).
nat(670).
nat(671).
nat(672).
nat(673).
nat(674).
nat(675).
nat(676).
nat(677).
nat(678).
nat(679).
nat(680).
nat(681).
nat(682).
nat(683).
nat(200).
nat(684).
nat(201).
nat(685).
nat(202).
nat(686).
nat(203).
nat(687).
nat(204).
nat(688).
nat(205).
nat(689).
nat(206).
nat(207).
nat(208).
nat(209).
nat(690).
nat(691).
nat(692).
nat(693).
nat(210).
nat(694).
nat(211).
nat(695).
nat(212).
nat(696).
nat(213).
nat(697).
nat(214).
nat(698).
nat(215).
nat(699).
nat(216).
nat(217).
nat(218).
nat(219).
nat(220).
nat(221).
nat(222).
nat(223).
nat(224).
nat(225).
nat(226).
nat(227).
nat(228).
nat(229).
nat(230).
nat(231).
nat(232).
nat(233).
nat(234).
nat(235).
nat(236).
nat(237).
nat(238).
nat(239).
nat(1909).
nat(1908).
nat(240).
nat(241).
nat(242).
nat(243).
nat(244).
nat(245).
nat(246).
nat(247).
nat(248).
nat(249).
nat(1907).
nat(1906).
nat(1905).
nat(1904).
nat(1903).
nat(1902).
nat(1901).
nat(1900).
nat(1919).
nat(250).
nat(251).
nat(252).
nat(253).
nat(1910).
nat(254).
nat(255).
nat(256).
nat(257).
nat(258).
nat(259).
nat(1918).
nat(1917).
nat(1916).
nat(1915).
nat(1914).
nat(1913).
nat(1912).
nat(1911).
nat(260).
nat(261).
nat(262).
nat(263).
nat(264).
nat(1921).
nat(265).
nat(1920).
nat(266).
nat(267).
nat(268).
nat(269).
nat(1929).
nat(1928).
nat(1927).
nat(1926).
nat(1925).
nat(1924).
nat(1923).
nat(1922).
nat(270).
nat(271).
nat(272).
nat(273).
nat(274).
nat(275).
nat(1932).
nat(276).
nat(1931).
nat(277).
nat(1930).
nat(278).
nat(279).
nat(1939).
nat(1938).
nat(1937).
nat(1936).
nat(1935).
nat(1934).
nat(1933).
nat(280).
nat(281).
nat(282).
nat(283).
nat(284).
nat(285).
nat(286).
nat(1943).
nat(287).
nat(1942).
nat(288).
nat(1941).
nat(289).
nat(1940).
nat(1949).
nat(1948).
nat(1947).
nat(1946).
nat(1945).
nat(1944).
nat(290).
nat(291).
nat(292).
nat(293).
nat(294).
nat(295).
nat(296).
nat(297).
nat(1954).
nat(298).
nat(1953).
nat(299).
nat(1952).
nat(1951).
nat(1950).
nat(1959).
nat(1958).
nat(1957).
nat(1956).
nat(1955).
nat(1965).
nat(1964).
nat(1963).
nat(1962).
nat(1961).
nat(1960).
nat(1969).
nat(1968).
nat(1967).
nat(1966).
nat(1976).
nat(1975).
nat(1974).
nat(1973).
nat(1972).
nat(1971).
nat(1970).
nat(1979).
nat(1978).
nat(1977).
nat(1990).
nat(1503).
nat(1987).
nat(1502).
nat(1986).
nat(1501).
nat(1985).
nat(1500).
nat(1984).
nat(1983).
nat(1982).
nat(1981).
nat(1980).
nat(1509).
nat(1508).
nat(1507).
nat(1506).
nat(1505).
nat(1989).
nat(1504).
nat(1988).
nat(1514).
nat(1998).
nat(1513).
nat(1997).
nat(1512).
nat(1996).
nat(1511).
nat(1995).
nat(1510).
nat(1994).
nat(1993).
nat(1992).
nat(1991).
nat(1519).
nat(1518).
nat(1517).
nat(1516).
nat(1515).
nat(1999).
nat(1525).
nat(1524).
nat(1523).
nat(1522).
nat(1521).
nat(1520).
nat(1529).
nat(1528).
nat(1527).
nat(1526).
nat(1536).
nat(1535).
nat(1534).
nat(1533).
nat(1532).
nat(1531).
nat(1530).
nat(1539).
nat(1538).
nat(1537).
nat(1550).
nat(1547).
nat(1546).
nat(1545).
nat(1544).
nat(1543).
nat(1542).
nat(1541).
nat(1540).
nat(1549).
nat(1548).
nat(1561).
nat(1560).
nat(1558).
nat(1557).
nat(1556).
nat(1555).
nat(1554).
nat(1553).
nat(1552).
nat(1551).
nat(1559).
nat(1572).
nat(1571).
nat(1570).
nat(1569).
nat(1568).
nat(1567).
nat(1566).
nat(1565).
nat(1564).
nat(1563).
nat(1562).
nat(1583).
nat(1582).
nat(1581).
nat(1580).
nat(1579).
nat(1578).
nat(1577).
nat(1576).
nat(1575).
nat(1574).
nat(1573).
nat(1110).
nat(1594).
nat(1593).
nat(1592).
nat(1591).
nat(1590).
nat(1107).
nat(1106).
nat(1105).
nat(1589).
nat(1104).
nat(1588).
nat(1103).
nat(1587).
nat(1102).
nat(1586).
nat(1101).
nat(1585).
nat(1100).
nat(1584).
nat(1109).
nat(1108).
nat(1121).
nat(1120).
nat(1118).
nat(1117).
nat(1116).
nat(1115).
nat(1599).
nat(1114).
nat(1598).
nat(1113).
nat(1597).
nat(1112).
nat(1596).
nat(1111).
nat(1595).
nat(1119).
nat(1132).
nat(1131).
nat(1130).
nat(1129).
nat(1128).
nat(1127).
nat(1126).
nat(1125).
nat(1124).
nat(1123).
nat(700).
nat(1122).
nat(701).
nat(702).
nat(703).
nat(704).
nat(705).
nat(706).
nat(707).
nat(708).
nat(709).
nat(1143).
nat(1142).
nat(1141).
nat(1140).
nat(1139).
nat(1138).
nat(1137).
nat(1136).
nat(1135).
nat(710).
nat(1134).
nat(711).
nat(1133).
nat(712).
nat(713).
nat(714).
nat(715).
nat(716).
nat(717).
nat(718).
nat(719).
nat(1154).
nat(1153).
nat(2000).
nat(1152).
nat(1151).
nat(1150).
nat(1149).
nat(1148).
nat(1147).
nat(720).
nat(1146).
nat(721).
nat(1145).
nat(722).
nat(1144).
nat(723).
nat(724).
nat(725).
nat(726).
nat(727).
nat(728).
nat(729).
nat(1165).
nat(1164).
nat(1163).
nat(1162).
nat(1161).
nat(1160).
nat(1159).
nat(730).
nat(1158).
nat(731).
nat(1157).
nat(732).
nat(1156).
nat(733).
nat(1155).
nat(734).
nat(735).
nat(736).
nat(737).
nat(738).
nat(739).
nat(1176).
nat(1175).
nat(1174).
nat(1173).
nat(1172).
nat(1171).
nat(1170).
nat(740).
nat(741).
nat(1169).
nat(742).
nat(1168).
nat(743).
nat(1167).
nat(744).
nat(1166).
nat(745).
nat(746).
nat(747).
nat(748).
nat(749).
nat(1187).
nat(1186).
nat(1185).
nat(1184).
nat(1183).
nat(1182).
nat(1181).
nat(1180).
nat(750).
nat(751).
nat(752).
nat(753).
nat(1179).
nat(754).
nat(1178).
nat(755).
nat(1177).
nat(756).
nat(757).
nat(758).
nat(759).
nat(1190).
nat(1198).
nat(1197).
nat(1196).
nat(1195).
nat(1194).
nat(1193).
nat(1192).
nat(1191).
nat(760).
nat(761).
nat(762).
nat(763).
nat(764).
nat(765).
nat(1189).
nat(766).
nat(1188).
nat(767).
nat(768).
nat(769).
nat(770).
nat(771).
nat(772).
nat(773).
nat(774).
nat(775).
nat(776).
nat(777).
nat(1199).
nat(778).
nat(779).
nat(780).
nat(781).
nat(782).
nat(783).
nat(300).
nat(784).
nat(301).
nat(785).
nat(302).
nat(786).
nat(303).
nat(787).
nat(304).
nat(788).
nat(305).
nat(789).
nat(306).
nat(307).
nat(308).
nat(309).
nat(790).
nat(791).
nat(792).
nat(793).
nat(310).
nat(794).
nat(311).
nat(795).
nat(312).
nat(796).
nat(313).
nat(797).
nat(314).
nat(798).
nat(315).
nat(799).
nat(316).
nat(317).
nat(318).
nat(319).
nat(320).
nat(321).
nat(322).
nat(323).
nat(324).
nat(325).
nat(326).
nat(327).
nat(328).
nat(329).
nat(330).
nat(331).
nat(332).
nat(333).
nat(334).
nat(335).
nat(336).
nat(337).
nat(338).
nat(339).
nat(340).
nat(341).
nat(342).
nat(343).
nat(344).
nat(345).
nat(346).
nat(347).
nat(348).
nat(349).
nat(350).
nat(351).
nat(352).
nat(353).
nat(354).
nat(355).
nat(356).
nat(357).
nat(358).
nat(359).
nat(10).
nat(11).
nat(12).
nat(13).
nat(14).
nat(15).
nat(16).
nat(17).
nat(18).
nat(19).
nat(360).
nat(361).
nat(362).
nat(363).
nat(364).
nat(365).
nat(366).
nat(367).
nat(368).
nat(369).
nat(20).
nat(21).
nat(22).
nat(23).
nat(24).
nat(25).
nat(26).
nat(27).
nat(28).
nat(29).
nat(370).
nat(371).
nat(372).
nat(373).
nat(374).
nat(375).
nat(376).
nat(377).
nat(378).
nat(379).
nat(30).
nat(31).
nat(32).
nat(33).
nat(34).
nat(35).
nat(36).
nat(37).
nat(38).
nat(39).
nat(380).
nat(381).
nat(382).
nat(383).
nat(384).
nat(385).
nat(386).
nat(387).
nat(388).
nat(389).
nat(40).
nat(41).
nat(42).
nat(43).
nat(44).
nat(45).
nat(46).
nat(47).
nat(48).
nat(49).
nat(390).
nat(391).
nat(392).
nat(393).
nat(394).
nat(395).
nat(396).
nat(397).
nat(398).
nat(399).
nat(50).
nat(51).
nat(52).
nat(53).
nat(54).
nat(55).
nat(56).
nat(57).
nat(58).
nat(59).
nat(60).
nat(61).
nat(62).
nat(63).
nat(64).
nat(65).
nat(66).
nat(67).
nat(68).
nat(69).
nat(70).
nat(71).
nat(72).
nat(73).
nat(74).
nat(75).
nat(76).
nat(77).
nat(78).
nat(79).
nat(1602).
nat(1601).
nat(1600).
nat(80).
nat(81).
nat(1609).
nat(82).
nat(1608).
nat(83).
nat(1607).
nat(84).
nat(1606).
nat(85).
nat(1605).
nat(86).
nat(1604).
nat(87).
nat(1603).
nat(88).
nat(89).
nat(1613).
nat(1612).
nat(1611).
nat(1610).
nat(90).
nat(91).
nat(92).
nat(93).
nat(1619).
nat(94).
nat(1618).
nat(95).
nat(1617).
nat(96).
nat(1616).
nat(97).
nat(1615).
nat(98).
nat(1614).
nat(99).
nat(1624).
nat(1623).
nat(1622).
nat(1621).
nat(1620).
nat(1629).
nat(1628).
nat(1627).
nat(1626).
nat(1625).
nat(1635).
nat(1634).
nat(1633).
nat(1632).
nat(1631).
nat(1630).
nat(1639).
nat(1638).
nat(1637).
nat(1636).
nat(1646).
nat(1645).
nat(1644).
nat(1643).
nat(1642).
nat(1641).
nat(1640).
nat(1649).
nat(1648).
nat(1647).
nat(1660).
nat(1657).
nat(1656).
nat(1655).
nat(1654).
nat(1653).
nat(1652).
nat(1651).
nat(1650).
nat(1659).
nat(1658).
nat(1671).
nat(1670).
nat(1668).
nat(1667).
nat(1666).
nat(1665).
nat(1664).
nat(1663).
nat(1662).
nat(1661).
nat(1669).
nat(1682).
nat(1681).
nat(1680).
nat(1679).
nat(1678).
nat(1677).
nat(1676).
nat(1675).
nat(1674).
nat(1673).
nat(1672).
nat(1693).
nat(1692).
nat(1691).
nat(1690).
nat(1206).
nat(1205).
nat(1689).
nat(1204).
nat(1688).
nat(1203).
nat(1687).
nat(1202).
nat(1686).
nat(1201).
nat(1685).
nat(1200).
nat(1684).
nat(1683).
nat(1209).
nat(1208).
nat(1207).
nat(1220).
nat(1217).
nat(1216).
nat(1215).
nat(1699).
nat(1214).
nat(1698).
nat(1213).
nat(1697).
nat(1212).
nat(1696).
nat(1211).
nat(1695).
nat(1210).
nat(1694).
nat(1219).
nat(1218).
nat(1231).
nat(1230).
nat(1228).
nat(1227).
nat(1226).
nat(1225).
nat(1224).
nat(1223).
nat(1222).
nat(1221).
nat(800).
nat(801).
nat(802).
nat(803).
nat(804).
nat(805).
nat(806).
nat(807).
nat(1229).
nat(808).
nat(809).
nat(1242).
nat(1241).
nat(1240).
nat(1239).
nat(1238).
nat(1237).
nat(1236).
nat(1235).
nat(1234).
nat(1233).
nat(810).
nat(1232).
nat(811).
nat(812).
nat(813).
nat(814).
nat(815).
nat(816).
nat(817).
nat(818).
nat(819).
nat(1253).
nat(1252).
nat(1251).
nat(1250).
nat(1249).
nat(1248).
nat(1247).
nat(1246).
nat(1245).
nat(820).
nat(1244).
nat(821).
nat(1243).
nat(822).
nat(823).
nat(824).
nat(825).
nat(826).
nat(827).
nat(828).
nat(829).
nat(1264).
nat(1263).
nat(1262).
nat(1261).
nat(1260).
nat(1259).
nat(1258).
nat(1257).
nat(830).
nat(1256).
nat(831).
nat(1255).
nat(832).
nat(1254).
nat(833).
nat(834).
nat(835).
nat(836).
nat(837).
nat(838).
nat(839).
nat(1275).
nat(1274).
nat(1273).
nat(1272).
nat(1271).
nat(1270).
nat(1269).
nat(840).
nat(1268).
nat(841).
nat(1267).
nat(842).
nat(1266).
nat(843).
nat(1265).
nat(844).
nat(845).
nat(846).
nat(847).
nat(848).
nat(849).
nat(1286).
nat(1285).
nat(1284).
nat(1283).
nat(1282).
nat(1281).
nat(1280).
nat(850).
nat(851).
nat(1279).
nat(852).
nat(1278).
nat(853).
nat(1277).
nat(854).
nat(1276).
nat(855).
nat(856).
nat(857).
nat(858).
nat(859).
nat(1297).
nat(1296).
nat(1295).
nat(1294).
nat(1293).
nat(1292).
nat(1291).
nat(1290).
nat(860).
nat(861).
nat(862).
nat(863).
nat(1289).
nat(864).
nat(1288).
nat(865).
nat(1287).
nat(866).
nat(867).
nat(868).
nat(869).
nat(870).
nat(871).
nat(872).
nat(873).
nat(874).
nat(875).
nat(1299).
nat(876).
nat(1298).
nat(877).
nat(878).
nat(879).
nat(880).
nat(881).
nat(882).
nat(883).
nat(400).
nat(884).
nat(401).
nat(885).
nat(402).
nat(886).
nat(403).
nat(887).
nat(404).
nat(888).
nat(405).
nat(889).
nat(406).
nat(407).
nat(408).
nat(409).
nat(890).
nat(891).
nat(892).
nat(893).
nat(410).
nat(894).
nat(411).
nat(895).
nat(412).
nat(896).
nat(413).
nat(897).
nat(414).
nat(898).
nat(415).
nat(899).
nat(416).
nat(417).
nat(418).
nat(419).
nat(420).
nat(421).
nat(422).
nat(423).
nat(424).
nat(425).
nat(426).
nat(427).
nat(428).
nat(429).
nat(430).
nat(431).
nat(432).
nat(433).
nat(434).
nat(435).
nat(436).
nat(437).
nat(438).
nat(439).
nat(440).
nat(441).
nat(442).
nat(443).
nat(444).
nat(445).
nat(446).
nat(447).
nat(448).
nat(449).
nat(450).
nat(451).
nat(452).
nat(453).
nat(454).
nat(455).
nat(456).
nat(457).
nat(458).
nat(459).
nat(460).
nat(461).
nat(462).
nat(463).
nat(464).
nat(465).
nat(466).
nat(467).
nat(468).
nat(469).
nat(470).
nat(471).
nat(472).
nat(473).
nat(474).
nat(475).
nat(476).
nat(477).
nat(478).
nat(479).
nat(480).
nat(481).
nat(482).
nat(483).
nat(484).
nat(485).
nat(486).
nat(487).
nat(488).
nat(489).
nat(490).
nat(491).
nat(492).
nat(493).
nat(494).
nat(495).
nat(496).
nat(497).
nat(498).
nat(499).
nat(1701).
nat(1700).
nat(1709).
nat(1708).
nat(1707).
nat(1706).
nat(1705).
nat(1704).
nat(1703).
nat(1702).
nat(1712).
nat(1711).
nat(1710).
nat(1719).
nat(1718).
nat(1717).
nat(1716).
nat(1715).
nat(1714).
nat(1713).
nat(1723).
nat(1722).
nat(1721).
nat(1720).
nat(1729).
nat(1728).
nat(1727).
nat(1726).
nat(1725).
nat(1724).
nat(1734).
nat(1733).
nat(1732).
nat(1731).
nat(1730).
nat(1739).
nat(1738).
nat(1737).
nat(1736).
nat(1735).
nat(1745).
nat(1744).
nat(1743).
nat(1742).
nat(1741).
nat(1740).
nat(1749).
nat(1748).
nat(1747).
nat(1746).
nat(1756).
nat(1755).
nat(1754).
nat(1753).
nat(1752).
nat(1751).
nat(1750).
nat(1759).
nat(1758).
nat(1757).
nat(1770).
nat(1767).
nat(1766).
nat(1765).
nat(1764).
nat(1763).
nat(1762).
nat(1761).
nat(1760).
nat(1769).
nat(1768).
nat(1781).
nat(1780).
nat(1778).
nat(1777).
nat(1776).
nat(1775).
nat(1774).
nat(1773).
nat(1772).
nat(1771).
nat(1779).
nat(1792).
nat(1791).
nat(1790).
nat(1305).
nat(1789).
nat(1304).
nat(1788).
nat(1303).
nat(1787).
nat(1302).
nat(1786).
nat(1301).
nat(1785).
nat(1300).
nat(1784).
nat(1783).
nat(1782).
nat(1309).
nat(1308).
nat(1307).
nat(1306).
nat(1316).
nat(1315).
nat(1799).
nat(1314).
nat(1798).
nat(1313).
nat(1797).
nat(1312).
nat(1796).
nat(1311).
nat(1795).
nat(1310).
nat(1794).
nat(1793).
nat(1319).
nat(1318).
nat(1317).
nat(1330).
nat(1327).
nat(1326).
nat(1325).
nat(1324).
nat(1323).
nat(1322).
nat(1321).
nat(1320).
nat(900).
nat(901).
nat(902).
nat(903).
nat(904).
nat(905).
nat(1329).
nat(906).
nat(1328).
nat(907).
nat(908).
nat(909).
nat(1341).
nat(1340).
nat(1338).
nat(1337).
nat(1336).
nat(1335).
nat(1334).
nat(1333).
nat(1332).
nat(1331).
nat(910).
nat(911).
nat(912).
nat(913).
nat(914).
nat(915).
nat(916).
nat(917).
nat(1339).
nat(918).
nat(919).
nat(1352).
nat(1351).
nat(1350).
nat(1349).
nat(1348).
nat(1347).
nat(1346).
nat(1345).
nat(1344).
nat(1343).
nat(920).
nat(1342).
nat(921).
nat(922).
nat(923).
nat(924).
nat(925).
nat(926).
nat(927).
nat(928).
nat(929).
nat(1363).
nat(1362).
nat(1361).
nat(1360).
nat(1359).
nat(1358).
nat(1357).
nat(1356).
nat(1355).
nat(930).
nat(1354).
nat(931).
nat(1353).
nat(932).
nat(933).
nat(934).
nat(935).
nat(936).
nat(937).
nat(938).
nat(939).
nat(1374).
nat(1373).
nat(1372).
nat(1371).
nat(1370).
nat(1369).
nat(1368).
nat(1367).
nat(940).
nat(1366).
nat(941).
nat(1365).
nat(942).
nat(1364).
nat(943).
nat(944).
nat(945).
nat(946).
nat(947).
nat(948).
nat(949).
nat(1385).
nat(1384).
nat(1383).
nat(1382).
nat(1381).
nat(1380).
nat(1379).
nat(950).
nat(1378).
nat(951).
nat(1377).
nat(952).
nat(1376).
nat(953).
nat(1375).
nat(954).
nat(955).
nat(956).
nat(957).
nat(958).
nat(959).
nat(1396).
nat(1395).
nat(1394).
nat(1393).
nat(1392).
nat(1391).
nat(1390).
nat(960).
nat(961).
nat(1389).
nat(962).
nat(1388).
nat(963).
nat(1387).
nat(964).
nat(1386).
nat(965).
nat(966).
nat(967).
nat(968).
nat(969).
nat(970).
nat(971).
nat(972).
nat(973).
nat(1399).
nat(974).
nat(1398).
nat(975).
nat(1397).
nat(976).
nat(977).
nat(978).
nat(979).
nat(980).
nat(981).
nat(982).
nat(983).
nat(500).
nat(984).
nat(501).
nat(985).
nat(502).
nat(986).
nat(503).
nat(987).
nat(504).
nat(988).
nat(505).
nat(989).
nat(506).
nat(507).
nat(508).
nat(509).
nat(990).
nat(991).
nat(992).
nat(993).
nat(510).
nat(994).
nat(511).
nat(995).
nat(512).
nat(996).
nat(513).
nat(997).
nat(514).
nat(998).
nat(515).
nat(999).
nat(516).
nat(517).
nat(518).
nat(519).
nat(520).
nat(521).
nat(522).
nat(523).
nat(524).
nat(525).
nat(526).
nat(527).
nat(528).
nat(529).
nat(530).
nat(531).
nat(532).
nat(533).
nat(534).
nat(535).
nat(536).
nat(537).
nat(538).
nat(539).
nat(540).
nat(541).
nat(542).
nat(543).
nat(544).
nat(545).
nat(546).
nat(547).
nat(548).
nat(549).
nat(550).
nat(551).
nat(552).
nat(553).
nat(554).
nat(555).
nat(556).
nat(557).
nat(558).
nat(559).
nat(560).
nat(561).
nat(562).
nat(563).
nat(564).
nat(565).
nat(566).
nat(567).
nat(568).
nat(569).
nat(570).
nat(571).
nat(572).
nat(573).
nat(574).
nat(575).
nat(576).
nat(577).
nat(578).
nat(579).
nat(580).
nat(581).
nat(582).
nat(583).
nat(100).
nat(584).
nat(101).
nat(585).
nat(102).
nat(586).
nat(103).
nat(587).
nat(104).
nat(588).
nat(105).
nat(589).
nat(106).
nat(107).
nat(108).
nat(109).
nat(590).
nat(591).
nat(592).
nat(593).
nat(110).
nat(594).
nat(111).
nat(595).
nat(112).
nat(596).
nat(113).
nat(597).
nat(114).
nat(598).
nat(115).
nat(599).
nat(116).
nat(117).
nat(118).
nat(119).
nat(120).
nat(121).
nat(122).
nat(123).
nat(124).
nat(125).
nat(126).
nat(127).
nat(128).
nat(129).
nat(130).
nat(131).
nat(132).
nat(133).
nat(134).
nat(135).
nat(136).
nat(137).
nat(138).
nat(139).
nat(1809).
nat(140).
nat(141).
nat(142).
nat(143).
nat(1800).
nat(144).
nat(145).
nat(146).
nat(147).
nat(148).
nat(149).
nat(1808).
nat(1807).
nat(1806).
nat(1805).
nat(1804).
nat(1803).
nat(1802).
nat(1801).
nat(150).
nat(151).
nat(152).
nat(153).
nat(154).
nat(1811).
nat(155).
nat(1810).
nat(156).
nat(157).
nat(158).
nat(159).
nat(1819).
nat(1818).
nat(1817).
nat(1816).
nat(1815).
nat(1814).
nat(1813).
nat(1812).
nat(160).
nat(161).
nat(162).
nat(163).
nat(164).
nat(165).
nat(1822).
nat(166).
nat(1821).
nat(167).
nat(1820).
nat(168).
nat(169).
nat(1829).
nat(1828).
nat(1827).
nat(1826).
nat(1825).
nat(1824).
nat(1823).
nat(170).
nat(171).
nat(172).
nat(173).
nat(174).
nat(175).
nat(176).
nat(1833).
nat(177).
nat(1832).
nat(178).
nat(1831).
nat(179).
nat(1830).
nat(1839).
nat(1838).
nat(1837).
nat(1836).
nat(1835).
nat(1834).
nat(180).
nat(181).
nat(182).
nat(183).
nat(184).
nat(185).
nat(186).
nat(187).
nat(1844).
nat(188).
nat(1843).
nat(189).
nat(1842).
nat(1841).
nat(1840).
nat(1849).
nat(1848).
nat(1847).
nat(1846).
nat(1845).
nat(190).
nat(191).
nat(192).
nat(193).
nat(194).
nat(195).
nat(196).
nat(197).
nat(198).
nat(1855).
nat(199).
nat(1854).
nat(1853).
nat(1852).
nat(1851).
nat(1850).
nat(1859).
nat(1858).
nat(1857).
nat(1856).
nat(1866).
nat(1865).
nat(1864).
nat(1863).
nat(1862).
nat(1861).
nat(1860).
nat(1869).
nat(1868).
nat(1867).
nat(1880).
nat(1877).
nat(1876).
nat(1875).
nat(1874).
nat(1873).
nat(1872).
nat(1871).
nat(1870).
nat(1879).
nat(1878).
nat(1891).
nat(1890).
nat(1404).
nat(1888).
nat(1403).
nat(1887).
nat(1402).
nat(1886).
nat(1401).
nat(1885).
nat(1400).
nat(1884).
nat(1883).
nat(1882).
nat(1881).
nat(1409).
nat(1408).
nat(1407).
nat(1406).
nat(1405).
nat(1889).
nat(1415).
nat(1899).
nat(1414).
nat(1898).
nat(1413).
nat(1897).
nat(1412).
nat(1896).
nat(1411).
nat(1895).
nat(1410).
nat(1894).
nat(1893).
nat(1892).
nat(1419).
nat(1418).
nat(1417).
nat(1416).
nat(1426).
nat(1425).
nat(1424).
nat(1423).
nat(1422).
nat(1421).
nat(1420).
nat(1429).
nat(1428).
nat(1427).
nat(1440).
nat(1437).
nat(1436).
nat(1435).
nat(1434).
nat(1433).
nat(1432).
nat(1431).
nat(1430).
nat(1439).
nat(1438).
nat(1451).
nat(1450).
nat(1448).
nat(1447).
nat(1446).
nat(1445).
nat(1444).
nat(1443).
nat(1442).
nat(1441).
nat(1449).
col(1).
col(2).
col(3).
col(4).
col(5).
col(6).
col(7).
col(8).
col(9).
col(500).
col(400).
col(401).
col(402).
col(403).
col(404).
col(405).
col(406).
col(407).
col(408).
col(409).
col(410).
col(411).
col(412).
col(413).
col(414).
col(415).
col(416).
col(417).
col(418).
col(419).
col(420).
col(300).
col(421).
col(301).
col(422).
col(302).
col(423).
col(303).
col(424).
col(304).
col(425).
col(305).
col(426).
col(306).
col(427).
col(307).
col(428).
col(308).
col(429).
col(309).
col(430).
col(310).
col(431).
col(311).
col(432).
col(312).
col(433).
col(313).
col(434).
col(314).
col(435).
col(315).
col(436).
col(316).
col(437).
col(317).
col(438).
col(318).
col(439).
col(319).
col(440).
col(320).
col(441).
col(200).
col(321).
col(442).
col(201).
col(322).
col(443).
col(202).
col(323).
col(444).
col(203).
col(324).
col(445).
col(204).
col(325).
col(446).
col(205).
col(326).
col(447).
col(206).
col(327).
col(448).
col(207).
col(328).
col(449).
col(208).
col(329).
col(209).
col(450).
col(330).
col(451).
col(210).
col(331).
col(452).
col(211).
col(332).
col(453).
col(212).
col(333).
col(454).
col(213).
col(334).
col(455).
col(214).
col(335).
col(456).
col(215).
col(336).
col(457).
col(216).
col(337).
col(458).
col(217).
col(338).
col(459).
col(218).
col(339).
col(219).
col(460).
col(340).
col(461).
col(220).
col(341).
col(462).
col(100).
col(221).
col(342).
col(463).
col(101).
col(222).
col(343).
col(464).
col(102).
col(223).
col(344).
col(465).
col(103).
col(224).
col(345).
col(466).
col(104).
col(225).
col(346).
col(467).
col(105).
col(226).
col(347).
col(468).
col(106).
col(227).
col(348).
col(469).
col(107).
col(228).
col(349).
col(108).
col(229).
col(109).
col(470).
col(350).
col(471).
col(230).
col(351).
col(472).
col(110).
col(231).
col(352).
col(473).
col(111).
col(232).
col(353).
col(474).
col(112).
col(233).
col(354).
col(475).
col(113).
col(234).
col(355).
col(476).
col(114).
col(235).
col(356).
col(477).
col(115).
col(236).
col(357).
col(478).
col(116).
col(237).
col(358).
col(479).
col(117).
col(238).
col(359).
col(118).
col(239).
col(119).
col(10).
col(11).
col(12).
col(13).
col(14).
col(15).
col(16).
col(17).
col(18).
col(19).
col(480).
col(360).
col(481).
col(240).
col(361).
col(482).
col(120).
col(241).
col(362).
col(483).
col(121).
col(242).
col(363).
col(484).
col(122).
col(243).
col(364).
col(485).
col(123).
col(244).
col(365).
col(486).
col(124).
col(245).
col(366).
col(487).
col(125).
col(246).
col(367).
col(488).
col(126).
col(247).
col(368).
col(489).
col(127).
col(248).
col(369).
col(128).
col(249).
col(129).
col(20).
col(21).
col(22).
col(23).
col(24).
col(25).
col(26).
col(27).
col(28).
col(29).
col(490).
col(370).
col(491).
col(250).
col(371).
col(492).
col(130).
col(251).
col(372).
col(493).
col(131).
col(252).
col(373).
col(494).
col(132).
col(253).
col(374).
col(495).
col(133).
col(254).
col(375).
col(496).
col(134).
col(255).
col(376).
col(497).
col(135).
col(256).
col(377).
col(498).
col(136).
col(257).
col(378).
col(499).
col(137).
col(258).
col(379).
col(138).
col(259).
col(139).
col(30).
col(31).
col(32).
col(33).
col(34).
col(35).
col(36).
col(37).
col(38).
col(39).
col(380).
col(260).
col(381).
col(140).
col(261).
col(382).
col(141).
col(262).
col(383).
col(142).
col(263).
col(384).
col(143).
col(264).
col(385).
col(144).
col(265).
col(386).
col(145).
col(266).
col(387).
col(146).
col(267).
col(388).
col(147).
col(268).
col(389).
col(148).
col(269).
col(149).
col(40).
col(41).
col(42).
col(43).
col(44).
col(45).
col(46).
col(47).
col(48).
col(49).
col(390).
col(270).
col(391).
col(150).
col(271).
col(392).
col(151).
col(272).
col(393).
col(152).
col(273).
col(394).
col(153).
col(274).
col(395).
col(154).
col(275).
col(396).
col(155).
col(276).
col(397).
col(156).
col(277).
col(398).
col(157).
col(278).
col(399).
col(158).
col(279).
col(159).
col(50).
col(51).
col(52).
col(53).
col(54).
col(55).
col(56).
col(57).
col(58).
col(59).
col(280).
col(160).
col(281).
col(161).
col(282).
col(162).
col(283).
col(163).
col(284).
col(164).
col(285).
col(165).
col(286).
col(166).
col(287).
col(167).
col(288).
col(168).
col(289).
col(169).
col(60).
col(61).
col(62).
col(63).
col(64).
col(65).
col(66).
col(67).
col(68).
col(69).
col(290).
col(170).
col(291).
col(171).
col(292).
col(172).
col(293).
col(173).
col(294).
col(174).
col(295).
col(175).
col(296).
col(176).
col(297).
col(177).
col(298).
col(178).
col(299).
col(179).
col(70).
col(71).
col(72).
col(73).
col(74).
col(75).
col(76).
col(77).
col(78).
col(79).
col(180).
col(181).
col(182).
col(183).
col(184).
col(185).
col(186).
col(187).
col(188).
col(189).
col(80).
col(81).
col(82).
col(83).
col(84).
col(85).
col(86).
col(87).
col(88).
col(89).
col(190).
col(191).
col(192).
col(193).
col(194).
col(195).
col(196).
col(197).
col(198).
col(199).
col(90).
col(91).
col(92).
col(93).
col(94).
col(95).
col(96).
col(97).
col(98).
col(99).
row(1).
row(2).
row(3).
row(4).
row(5).
row(6).
row(7).
row(8).
row(9).
row(500).
row(400).
row(401).
row(402).
row(403).
row(404).
row(405).
row(406).
row(407).
row(408).
row(409).
row(410).
row(411).
row(412).
row(413).
row(414).
row(415).
row(416).
row(417).
row(418).
row(419).
row(420).
row(300).
row(421).
row(301).
row(422).
row(302).
row(423).
row(303).
row(424).
row(304).
row(425).
row(305).
row(426).
row(306).
row(427).
row(307).
row(428).
row(308).
row(429).
row(309).
row(430).
row(310).
row(431).
row(311).
row(432).
row(312).
row(433).
row(313).
row(434).
row(314).
row(435).
row(315).
row(436).
row(316).
row(437).
row(317).
row(438).
row(318).
row(439).
row(319).
row(440).
row(320).
row(441).
row(200).
row(321).
row(442).
row(201).
row(322).
row(443).
row(202).
row(323).
row(444).
row(203).
row(324).
row(445).
row(204).
row(325).
row(446).
row(205).
row(326).
row(447).
row(206).
row(327).
row(448).
row(207).
row(328).
row(449).
row(208).
row(329).
row(209).
row(450).
row(330).
row(451).
row(210).
row(331).
row(452).
row(211).
row(332).
row(453).
row(212).
row(333).
row(454).
row(213).
row(334).
row(455).
row(214).
row(335).
row(456).
row(215).
row(336).
row(457).
row(216).
row(337).
row(458).
row(217).
row(338).
row(459).
row(218).
row(339).
row(219).
row(460).
row(340).
row(461).
row(220).
row(341).
row(462).
row(100).
row(221).
row(342).
row(463).
row(101).
row(222).
row(343).
row(464).
row(102).
row(223).
row(344).
row(465).
row(103).
row(224).
row(345).
row(466).
row(104).
row(225).
row(346).
row(467).
row(105).
row(226).
row(347).
row(468).
row(106).
row(227).
row(348).
row(469).
row(107).
row(228).
row(349).
row(108).
row(229).
row(109).
row(470).
row(350).
row(471).
row(230).
row(351).
row(472).
row(110).
row(231).
row(352).
row(473).
row(111).
row(232).
row(353).
row(474).
row(112).
row(233).
row(354).
row(475).
row(113).
row(234).
row(355).
row(476).
row(114).
row(235).
row(356).
row(477).
row(115).
row(236).
row(357).
row(478).
row(116).
row(237).
row(358).
row(479).
row(117).
row(238).
row(359).
row(118).
row(239).
row(119).
row(10).
row(11).
row(12).
row(13).
row(14).
row(15).
row(16).
row(17).
row(18).
row(19).
row(480).
row(360).
row(481).
row(240).
row(361).
row(482).
row(120).
row(241).
row(362).
row(483).
row(121).
row(242).
row(363).
row(484).
row(122).
row(243).
row(364).
row(485).
row(123).
row(244).
row(365).
row(486).
row(124).
row(245).
row(366).
row(487).
row(125).
row(246).
row(367).
row(488).
row(126).
row(247).
row(368).
row(489).
row(127).
row(248).
row(369).
row(128).
row(249).
row(129).
row(20).
row(21).
row(22).
row(23).
row(24).
row(25).
row(26).
row(27).
row(28).
row(29).
row(490).
row(370).
row(491).
row(250).
row(371).
row(492).
row(130).
row(251).
row(372).
row(493).
row(131).
row(252).
row(373).
row(494).
row(132).
row(253).
row(374).
row(495).
row(133).
row(254).
row(375).
row(496).
row(134).
row(255).
row(376).
row(497).
row(135).
row(256).
row(377).
row(498).
row(136).
row(257).
row(378).
row(499).
row(137).
row(258).
row(379).
row(138).
row(259).
row(139).
row(30).
row(31).
row(32).
row(33).
row(34).
row(35).
row(36).
row(37).
row(38).
row(39).
row(380).
row(260).
row(381).
row(140).
row(261).
row(382).
row(141).
row(262).
row(383).
row(142).
row(263).
row(384).
row(143).
row(264).
row(385).
row(144).
row(265).
row(386).
row(145).
row(266).
row(387).
row(146).
row(267).
row(388).
row(147).
row(268).
row(389).
row(148).
row(269).
row(149).
row(40).
row(41).
row(42).
row(43).
row(44).
row(45).
row(46).
row(47).
row(48).
row(49).
row(390).
row(270).
row(391).
row(150).
row(271).
row(392).
row(151).
row(272).
row(393).
row(152).
row(273).
row(394).
row(153).
row(274).
row(395).
row(154).
row(275).
row(396).
row(155).
row(276).
row(397).
row(156).
row(277).
row(398).
row(157).
row(278).
row(399).
row(158).
row(279).
row(159).
row(50).
row(51).
row(52).
row(53).
row(54).
row(55).
row(56).
row(57).
row(58).
row(59).
row(280).
row(160).
row(281).
row(161).
row(282).
row(162).
row(283).
row(163).
row(284).
row(164).
row(285).
row(165).
row(286).
row(166).
row(287).
row(167).
row(288).
row(168).
row(289).
row(169).
row(60).
row(61).
row(62).
row(63).
row(64).
row(65).
row(66).
row(67).
row(68).
row(69).
row(290).
row(170).
row(291).
row(171).
row(292).
row(172).
row(293).
row(173).
row(294).
row(174).
row(295).
row(175).
row(296).
row(176).
row(297).
row(177).
row(298).
row(178).
row(299).
row(179).
row(70).
row(71).
row(72).
row(73).
row(74).
row(75).
row(76).
row(77).
row(78).
row(79).
row(180).
row(181).
row(182).
row(183).
row(184).
row(185).
row(186).
row(187).
row(188).
row(189).
row(80).
row(81).
row(82).
row(83).
row(84).
row(85).
row(86).
row(87).
row(88).
row(89).
row(190).
row(191).
row(192).
row(193).
row(194).
row(195).
row(196).
row(197).
row(198).
row(199).
row(90).
row(91).
row(92).
row(93).
row(94).
row(95).
row(96).
row(97).
row(98).
row(99).
goal_(X1):-goal(X1).
goal__(X1):--goal(X1).
success_(X1):-success(X1).
success__(X1):--success(X1).
holds_(X1,X2):-holds(X1,X2).
holds__(X1,X2):--holds(X1,X2).
occurs_(X1,X2):-occurs(X1,X2).
occurs__(X1,X2):--occurs(X1,X2).
animate_(X1,X2):-animate(X1,X2).
animate__(X1,X2):--animate(X1,X2).
frame__(X1):-frame(X1).
frame___(X1):--frame(X1).
% goal_ goal__ success_ success__ holds_ holds__ occurs_ occurs__ animate_ animate__ frame__ frame___