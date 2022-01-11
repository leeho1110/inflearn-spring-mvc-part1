# spring-mvc

**Servlet**

서버에서는 단순 비즈니스 로직 외에 TCP/IP 대기 및 소켓 연결, HTTP 요청 메시지 파싱, HTTP 메시지 바디 내용 파싱 등등 모든 걸 해준다.

- HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
- HTTP 응답 정보를 편리하게 사용할 수 있는 HttpServletResponse

위 두개를 통해 HTTP 스펙을 매우 편리하게 사용할 수 있다. 

**HTTP 요청시 WAS에서 일어나는 일**

1. WAS는 Request, Response 객체를 새로 만들어 Servlet 객체 호출
2. 개발자는 Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
3. 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력
4. WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성

**Servlet Container**
- Tomcat처럼 Servlet을 지원하는 WAS를 Servlet Containker라고 함
- 컨테이너는 Servlet 객체를  생성, 초기화 호출, 종료하는 생명주기를 관리해줌
- Serlvet 객체는 싱글톤으로 관리된다. 고객의 요청이 올때마다 객체를 생성하는 것은 비효율적이기 때문에. 따라서 
최초 로딩 시점에 Servlet 객체를 미리 만들어주고 재활용한다, 또한 동시 요청을 위한 멀티 쓰레드 처리도 지원한다. 
즉, 모든 고객 요청은 동일한 Servelt 객체 인스턴스에 접근한다. 따라서 공유 변수 사용 시 주의해야한다.

---

**Thread**

아까 HTTP 요청이 오면 서블릿 객체를 호출한다고 했다. 그렇다면 이 호출은 누가 하는 것일까? 바로 **쓰레드**가 한다. 
요청이 도착할 때마다 쓰레드를 통해 요청을 실행해야한다. 하지만 무한정 쓰레드를 생성하는 것은 리소스의 부하를 유발하므로 우리는
**Thread Pool**을 통해 미리 생성해놓고 요청에 대응한다. 

여러 요청이 올 때 쓰레드 풀에서 생성되어있는 쓰레드를 해당 요청에 할당하고 만약 쓰레드 풀에 남은 쓰레드가 없는 경우는 
기다리도록 대기시키거나 거절할 수 있다. 따라서 WAS의 주요 튜닝 포인트는 **최대 쓰레드(max thread) 수** 이다.

---

**HTML Request, HTTP API, CSR, SSR**

API(Json), JSP & Thymeleaf

---

**MVC 패턴**

1. **너무 많은 역할**
   1. 하나의 서블릿이나 JSP만으로 비즈니스 로직 + 뷰 렌더링까지 모두 처리하면 너무 많은 역할로 인해 유지보수가 어려워진다.
2. **변경의 라이프 사이클**
   1. 둘의 변경 라이프 사이클은 다르다. UI를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 높고 서로 영향을 주지 않는다.
3. **기능 특화**
   1. JSP같은 뷰 템플릿은 화면을 렌더링하는데에 최적화 되어있기 때문에 각 업무만 담당하는 것이 가장 효과적

>**Model View Controller**

---

**DispacherServlet**

`org.springframework.web.servlet.DispatcherServlet`

스프링 MVC는 앞서 구현한 내용처럼 프론트 컨트롤러 패턴으로 구현이 되어있고, 그 컨트롤러가 바로 위의 디스패처 서블릿이다.
`DispatcherServlet`도 부모 클래스에서 `HttpServlet`을 상속받아서 사용하고 서블릿으로 동작한다.
> DispatcherServlet -> FrameworkServlet -> HttpServletBean -> HttpServlet

**요청 흐름**
1. 서블릿 호출되면 `HttpServlet`이 제공하는 `service()` 메소드가 호출된다.
2. 스프링 MVC는 DispatcherServlet 의 부모인 FrameworkServlet 에서 service() 를 오버라이드
   해두었다.
3. FrameworkServlet.service() 를 시작으로 여러 메서드가 호출되면서 DispacherServlet.doDispatch() 가 호출된다.

**Spring MVC 동작 구조**
1. 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2. 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
3. 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
5. `ModelAndView` 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 `ModelAndView`로 변환해서
   반환한다.
6. `viewResolver` 호출: 뷰 리졸버를 찾고 실행한다.
   JSP의 경우: `InternalResourceViewResolver` 가 자동 등록되고, 사용된다.
7. View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를
   반환한다.
   JSP의 경우 `InternalResourceView(JstlView)` 를 반환하는데, 내부에 forward() 로직이 있다.
8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다