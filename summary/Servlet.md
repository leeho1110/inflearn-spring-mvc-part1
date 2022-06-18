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

### Servlet, Template engine(Thymeleaf, JSP, …), MVC Pattern

- 강의에선 `PrintWriter` 클래스와 `HttpServletResponse` 의 getWriter()를 통해 HTTP 응답 메시지를 작성했다.
    - 하지만 이 방법은 HTML의 각 라인을 직접 작성해야 하기 때문에 매우 복잡하고 불편하다. **생산성이 아주 떨어지는 방법**이다.
    - 또한 **동적인 HTML 페이지 구성 역시 불가능**하다.
    - 따라서 이를 JSP, Thymeleaf, Freemarker, Velocity 와 같은 템플릿 엔진에게 위임한다.
    - JSP를 사용하기 위해선 `org.apache.tomcat.embed:tomcat-embed-jasper` 엔진을 추가해줘야 한다.
        - JSP의 경우 JSP → Servlet 과정을 거쳐, HTML 형태로 VIEW단에 나타나게 된다.
        - 이 과정에서 JSP를 Servlet으로 변환시켜주는 과정을 JSP 엔진이 수행한다.
        - Spring Boot은 Spring Framework(외장 톰캣 사용)와 다르게 Embeded Tomcat(내장 톰캣)으로 서버를 구동한다. 하지만 Embeded Tomcat은 Jasper 엔진을 탑재하고 있지 않아 추가로 Jasper 엔진에 대한 의존성을 추가해줘야 한다.
        - 외장 톰캣의 라이브러리에는 jasper.jar 파일이 기본적으로 탑재되어 있고 `javax.servlet.jsp.JspWriter`가 write() 메서드로 JSP 파일을 Servlet으로 변환한다.
- 애석하게도 JSP로 작성하는 방식 역시 문제가 존재한다.
    - 물론 view 페이지를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용할 수 있다는 장점이 있다.
    - 하지만 java 코드로 작성된 비즈니스 로직, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다. JSP가 너무 많은 역할을 한다.
    - 따라서 비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 관심사를 분리한다면 어떨까?
    - 위 문제를 해결하기 위해 **MVC 패턴**이 등장한다.
- **MVC(Model-View-Controller) 패턴**
    - Servlet, JSP 둘다 하나만 가지고 작업하기엔 **너무 많은 역할**을 담당한다.
        - 하나의 서블릿 혹은 JSP 파일로 비즈니스 로직과 뷰 렌더링에 대한 코드를 작성하는 경우 너무 많은 역할을 담당한다. 이는 개발 생산성 하락과 유지보수의 어려움을 유발한다.
        - 만약 비즈니스 로직이 변경된다면 비즈니스 로직 부분만 변경되고, UI를 변경할 일이 있는 경우는 UI 부분만 변경되어야 한다.
        - 하지만 이 둘이 합쳐져 있다면 정말 골때리는 일이다.
    - 또한 비즈니스 로직과 뷰 렌더링(UI)는 변경되는 라이프사이클이 다르다.
        - 여기서 SOLID 중 S, **Single Responsibility Principle** 의 향기가 살짝 난다. 위 이론은 객체는 한 가지의 책임(변경 이유)를 가져야 한다는 객체 지향 설계 원칙이다.
        - 만약 JSP 내에 비즈니스 로직과 뷰 렌더링에 관련된 코드가 존재한다면 이 코드는 두 가지의 책임(변경 이유)를 갖게 된다. 정확히는 일치하지 않지만 하나의 변경 이유만 존재하는 것이 코드의 유지보수성을 상승시키고 유연하게 변화하도록 가능케 하는 점이라는 변함이 없다. 따라서 위 둘은 분리되어야 한다.
    - MVC 패턴은 하나의 Servlet 혹은 JSP 파일로 다양한 책임을 처리하던 것을 **컨트롤러(Controller)와 뷰(View)**라는 영역으로 서로 역할을 나눈 것을 말한다.
        - **컨트롤러(Controller, C)**는 HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
        - **모델(Model, M)**은 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
        - **뷰(View, V)**: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.
    - 여기서 질문이 생길 수도 있다. 컨트롤러에 비즈니스 로직을 두면 안될까? 물론 가능하다. 둘 수도 있지만, 이렇게 되면 위에서 우리가 개선한 문제에서처럼 컨트롤러가 너무 많은 역할을 담당한다.
        - 따라서 비즈니스 로직은 **서비스** 라는 계층을 별도로 만들어 처리한다. 그리고 컨트롤러는
        비즈니스 로직이 있는 서비스를 호출하는 역할을 담당한다.
    - 하지만 MVC 패턴 역시 한계가 있다. 바로 컨트롤러가 수행하는 공통 기능을 매번 작성한다는 점이다.
        - 각 서블릿의 service()마다 **dispatcher.forward(request, response)를 호출**해야 한다.
        - **반환하는 뷰 페이지의 경로(*/WEB-INF/views/**)가 중복**된다.
        - 사용하지 않는 **HttpServletRequest, HttpServletResponse 인자**를 넣어준다.
    - 즉 컨트롤러를 호출하기 전에 공통 기능을 처리하는 수문장 역할을 담당하는 **프론트 컨트롤러(Front Controller)**가 등장했다.
- **Front Controller**
    - 현재의 컨트롤러 구조는 정해진 입구가 없는 상황이다. 각각의 컨트롤러로 클라이언트가 언제든 들어올 수 있는 상황이다.
        - 이를 각 컨트롤러의 공통 로직을 담당하는 서블릿 하나를 만들어 몰아넣고, 각 컨트롤러는 본인만의 로직을 수행하도록 했다.
        - 즉 프론트 컨트롤러가 모든 클라이언트의 요청을 받고, 해당 요청에 맞는 컨트롤러를 공통 로직을 처리하는 서블릿(프론트 컨트롤러)이 찾아 호출하게 되는 것이다.
        - 스프링에선 이 서블릿을 **DispatcherServlet**이라고 한다.
        - 스프링 부트는 DispatcherServlet을 빈 객체로 자동 등록하면서 **모든 경로에 대해 요청을 받도록 매핑**된다.