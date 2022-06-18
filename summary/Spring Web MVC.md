### Spring Web MVC

- 그런데 잘 생각해보면 DispatcherServlet(프론트 컨트롤러)도 결국 꽤 많은 책임을 지게 된다.
    - 클라이언트의 요청을 처리해야할 컨트롤러를 찾는 책임
    - 컨트롤러가 요청을 처리할 수 있도록 명령하는 책임
    - 컨트롤러가 반환한 뷰 페이지를 클라이언트에게 전달하는 책임
- 그래서 우린 다시 책임을 나누게 된다.
    - 요청을 처리할 컨트롤러를 찾는 책임은 **HandlerMapping**에게 할당
        - 대부분의 실무 컨트롤러에서는 `@RequestMapping`을 통해 URL을 매핑
        - ***@RequestMapping***
            
            > *Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures.
            요청 핸들러 클래스에 작성된 메서드에 웹 request를 매핑하기 위한 어노테이션입니다. 
            
            Both Spring MVC and Spring WebFlux support this annotation through a RequestMappingHandlerMapping and RequestMappingHandlerAdapter in their respective modules and package structure. For the exact list of supported handler method arguments and return types in each, please use the reference documentation links below:
            Spring MVC와 Spring WebFlux는 둘다 각자의 모듈과 패키지 구조 내부에 있는 RequestMappingHandlerMapping 및 RequestMappingHandlerAdapter 를 통해 이 어노테이션을 지원합니다.
            
            - Spring MVC [Method Arguments](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments)  and [Return Values](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types)
            - Spring WebFlux [Method Arguments](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-arguments)  and [Return Values](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-return-types)
            
            Note: This annotation can be used both at the class and at the method level. In most cases, at the method level applications will prefer to use one of the HTTP method specific variants @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, or @PatchMapping.
            이 어노테이션은 클래스와 메서드 둘다 사용이 가능합니다. 대부분 메서드 레벨에서의 애플리케이션들은 @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, or @PatchMapping 중 하나를 사용하는 것을 선호할 것입니다.
            
            NOTE: When using controller interfaces (e.g. for AOP proxying), make sure to consistently put all your mapping annotations - such as @RequestMapping and @SessionAttributes - on the controller interface rather than on the implementation class.
            컨트롤러 인터페이스(예: AOP 프록시)를 사용할 때는 모든 매핑 어노테이션(예: @RequestMapping 및 @SessionAttributes)을 구현 클래스가 아닌 컨트롤러 인터페이스에 일관되게 배치해야 합니다.*
            > 
        - `RequestMappingHandlerMapping`
    - 컨트롤러를 실행하는 것은 **HadlerAdapter**에게 할당
        - `RequestMappingHandlerAdapter`
    - 클라이언트에게 뷰 페이지를 전달하는 것은 **ViewResolver**에게 할당
- 스프링에서는 로그를 어떻게 기록할까?
    - 스프링 부트에서는 보통 **로깅 라이브러리(spring-boot-starter-logging)**를 사용한다.
        - **SLF4J**: java.util.logging, logback 및 log4j와 같은 다양한 로깅 프레임 워크에 대한추상화(인터페이스) 역할을 하는 라이브러리, 롬복을 통해서 @Slf4j 로 작성 가능
            
            > **Causes lombok to generate a logger field.**
            *lombok에서 인스턴스 필드에 logger를 생성합니다.
            
            Example:
            @Slf4j
            public class LogExample {
            }
            
            will generate:
            public class LogExample {
            private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
            }*
            > 
        - **Logback**