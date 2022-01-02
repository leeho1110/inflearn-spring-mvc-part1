# spring-mvc

**Servlet**

서버에서는 단순 비즈니스 로직 외에 TCP/IP 대기 및 소켓 연결, HTTP 요청 메시지 파싱, HTTP 메시지 바디 내용 파싱 등등 모든 걸 해준다.

- HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
- HTTP 응답 정보를 편리하게 사용할 수 있는 HttpServletResponse

위 두개를 통해 HTTP 스펙을 매우 편리하게 사용할 수 있다. 

- **HTTP 요청시 WAS에서 일어나는 일**
    1. WAS는 Request, Response 객체를 새로 만들어 Servlet 객체 호출
    2. 개발자는 Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
    3. 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력
    4. WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성

- **Servlet Container**
    - Tomcat처럼 Servlet을 지원하는 WAS를 Servlet Containker라고 함
    - 컨테이너는 Servlet 객체를  생성, 초기화 호출, 종료하는 생명주기를 관리해줌
    - Serlvet 객체는 싱글톤으로 관리된다. 고객의 요청이 올때마다 객체를 생성하는 것은 비효율적이기 때문에. 따라서 
  최초 로딩 시점에 Servlet 객체를 미리 만들어주고 재활용한다, 또한 동시 요청을 위한 멀티 쓰레드 처리도 지원한다. 
  즉, 모든 고객 요청은 동일한 Servelt 객체 인스턴스에 접근한다. 따라서 공유 변수 사용 시 주의해야한다.
