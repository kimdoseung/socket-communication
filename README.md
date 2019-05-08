# socket-communication

소켓을 통해서 화면을 스크린샷으로 찍어서 실시간으로 전송하고 채팅하는 기능을 구현해봤습니다.
화면은 한명(여기서는 호스트 서버라고했습니다)만 여러명(클라이언트서버라고 했습니다)한태 전송하고 여러명이 한명의 화면을 받아볼수있습니다.
채팅은 모두 서로 가능합니다.
화면(이미지)을 전송하는 쓰레드와 채팅을 전송하는 쓰레드가 각각 있습니다.

<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMTg2/MDAxNTU3Mjg3OTQ2NDQ1.Eq9fqHNnKT5xhdhgHgur0Sc5jtUradC4hbcg5JBBGU0g.x7s5YdHPGR_F8Rl26KAVlSChSXxqQBaoAS3sBLeYKlUg.PNG.p8137213/%EC%86%8C%EC%BC%93%ED%98%B8%EC%8A%A4%ED%8A%B8.png?type=w773">
화면을 다른사람들한태 전송해주는 호스트의 gui창입니다 화면을 안받아보기때문에 채팅창만있습니다.

<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjk5/MDAxNTU3Mjg3Nzc1NjE0.inFRraC_RlqQ17uro5TM3eesoIeQim42rnQ756BLXWgg.qb3Pbcl5lhWNsFxQW0OQPv-YSRYvfJQ1difOYjb2osUg.PNG.p8137213/%EC%86%8C%EC%BC%93%ED%86%B5%EC%8B%A0%ED%99%94%EB%A9%B4.png?type=w773">
클라이언트의 gui로 화면을 받는창과 옆에 채팅창이있습니다.
