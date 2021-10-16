# P2PNetworkInJava
2021 University Distributed System Computing Project

블록체인 기반 암호화폐 시스템

- Member Node : 코인 채굴, 트랜잭션 발생, 블록에 트랜잭션 기록 등
- Consortium Node : BlockChain, Broker Node관리, Consortium간 P2P 통신 담당.
 
[communication]
Consortium Node - Member Node : Server-Client 구조
Consortium Node - Consortium Node : P2P 

![그림1](https://user-images.githubusercontent.com/49053341/137587384-9dbae3f7-da12-4320-9b25-5bf1a09b70a0.png)

[노드 규모]
Consortium Node as Machine
Member Node as Machine


[서브시스템 동작]
![캡처2](https://user-images.githubusercontent.com/49053341/137587566-c3880b48-48d5-4ea6-9e45-c3e26bbf145e.JPG)



[사용 기술]

- 포트포워딩
- 암호화 : 트랜잭션 private key로 인봉하여 broker node에 전달
- P2P Networking



[실행]
![캡처](https://user-images.githubusercontent.com/49053341/137587545-6e78405a-2fe9-41fa-b8b7-0f627620b93d.JPG)



