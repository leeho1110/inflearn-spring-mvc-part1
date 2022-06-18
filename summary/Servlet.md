### Servlet

- 서블릿은 웹 애플리케이션을 구성하는 데에 필요한 코드 중 비즈니스 로직에만 집중할 수 있도록 아래와 같은 일을 수행해준다.
    - TCP/IP 대기 및 소켓 연결
    - HTTP 요청 메시지 파싱
    - HTTP 메시지 바디 파싱
    - 기타 등등
- **Servlet Container**
    - Tomcat처럼 Servlet을 지원하는 WAS를 Servlet Containker라고 함
    - 컨테이너는 Servlet 객체를 생성, 초기화 호출, 종료하는 생명주기를 관리해줌
    - Serlvet 객체는 싱글톤으로 관리된다. 고객의 요청이 올때마다 객체를 생성하는 것은 비효율적이기 때문이다. 앞서 스프링 핵심 원리에도 언급된 내용이고, 토비의 스프링에서도 언급된다.
        - 주로 싱글톤으로 생성되어 비즈니스 로직을 처리하는 객체들을 ‘서비스 오브젝트'라고 부른다.
        - 최초 로딩 시점에 Servlet 객체를 미리 만들어주고 재활용한다, 또한 동시 요청을 위한 멀티 쓰레드 처리도 지원한다.
        - 즉, 모든 고객 요청은 동일한 Servelt 객체 인스턴스에 접근한다. 따라서 공유 변수 사용 시 주의해야한다.
- **Thread**
    - 아까 HTTP 요청이 오면 서블릿 객체를 호출한다고 했다. 그렇다면 이 호출은 누가 하는 것일까? 바로 **쓰레드**가 한다.
    - 요청이 도착할 때마다 쓰레드를 통해 요청을 실행해야한다. 하지만 무한정 쓰레드를 생성하는 것은 리소스의 부하를 유발하므로 우리는 **Thread Pool**을 통해 미리 생성해놓고 요청에 대응한다.
    - 여러 요청이 올 때 쓰레드 풀에서 생성되어있는 쓰레드를 해당 요청에 할당하고 만약 쓰레드 풀에 남은 쓰레드가 없는 경우는 기다리도록 대기시키거나 거절할 수 있다.
        - 따라서 WAS의 주요 튜닝 포인트는 **최대 쓰레드(max thread) 수**이다.

---

### Servlet in Spring

- 스프링 부트에서는 서블릿을 유저가 등록해 사용할 수 있도록 지원한다.
    - ***@ServletComponentScan***
        
        > *Enables scanning for Servlet components (filters, servlets, and listeners). Scanning is only performed when using an embedded web server.
        서블릿 컴포넌트들(@WebServlet, @WebFilter, @WebListener)의 스캔을 가능케 해줍니다. 스캐닝은 내장 웹 서버를 사용하는 경우에만 수행됩니다.
        
        Typically, one of value, basePackages, or basePackageClasses should be specified to control the packages to be scanned for components. In their absence, scanning will be performed from the package of the class with the annotation.
        일반적으로 컴포넌트 스캔의 대상이 되고자 하는 패키지를 제어하려면 value, basePackages 또는 basePackageClass 중 하나를 지정해야 합니다. 만약 없을 경우 주석이 위치한 클래스의 패키지에서 스캔이 수행됩니다.*
        > 
- 스프링에서 서블릿을 등록하기 위해선 두 가지가 필요하다.
    - HttpServlet 클래스를 상속받기
    - ***@WebServlet*** 어노테이션 선언하기
        - **name** : ****서블릿의 이름
        - **urlPatterns** : 매핑할 URL
        
        > *This annotation is used to declare the configuration of a javax.servlet.Servlet. 
        If the name attribute is not defined, the fully qualified name of the class is used. 
        이 어노테이션은 javax.servelt.Servlet의 구성을 선언하기 위해 사용됩니다.
        
        At least one URL pattern MUST be declared in either the value or urlPattern attribute of the annotation, but not both. 
        적어도 어노테이션 속성으로 value, urlPattern 중 하나에 반드시 URL Pattern을 선언해야 합니다. 하지만 둘 다 선언할 수는 없습니다.
        
        The value attribute is recommended for use when the URL pattern is the only attribute being set, otherwise the urlPattern attribute should be used. 
        value는 다른 애트리뷰트 없이 사용될 때만 권장되며 나머지는 urlPattern을 반드시 사용해야 합니다.
        
        The class on which this annotation is declared MUST extend javax.servlet.http.HttpServlet.
        이 어노테이션이 선언된 클래스는 반드시 javax.servlet.http.HttpServlet 클래스를 상속해야 합니다.*
        > 
- 스프링은 HTTP 메시지를 손쉽게 사용할 수 있는 클래스를 제공한다. 아래 두개를 통해 HTTP 스펙을 매우 편리하게 사용할 수 있다.
    - HTTP 요청 정보를 편리하게 사용할 수 있는 `HttpServletRequest`
    - HTTP 응답 정보를 편리하게 사용할 수 있는 `HttpServletResponse`

---

**Thread**

아까 HTTP 요청이 오면 서블릿 객체를 호출한다고 했다. 그렇다면 이 호출은 누가 하는 것일까? 바로 **쓰레드**가 한다. 
요청이 도착할 때마다 쓰레드를 통해 요청을 실행해야한다. 하지만 무한정 쓰레드를 생성하는 것은 리소스의 부하를 유발하므로 우리는
**Thread Pool**을 통해 미리 생성해놓고 요청에 대응한다. 

여러 요청이 올 때 쓰레드 풀에서 생성되어있는 쓰레드를 해당 요청에 할당하고 만약 쓰레드 풀에 남은 쓰레드가 없는 경우는 
기다리도록 대기시키거나 거절할 수 있다. 따라서 WAS의 주요 튜닝 포인트는 **최대 쓰레드(max thread) 수** 이다.