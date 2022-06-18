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
- 클라이언트에게 응답값을 HTTP Body에 담아서 보내고 싶을 땐 **@RestController**을 사용하자.
    - @RestController 역시 똑같은 @Controller다. 다만 @ResponseBody를 곁들인
        - ***@RestController***
            
            > *A convenience annotation that is itself annotated with @Controller and @ResponseBody.
            @Controller와 @ResponseBody가 합쳐진 편리한 주석입니다.
            
            Types that carry this annotation are treated as controllers where @RequestMapping methods assume @ResponseBody semantics by default.
            이 어노테이션을 포함하는 유형은 @RequestMapping 메서드가 기본적으로 @ResponseBody 의미를 가정하는 컨트롤러로 간주됩니다.
            
            NOTE: @RestController is processed if an appropriate HandlerMapping-HandlerAdapter pair is configured such as the RequestMappingHandlerMapping-RequestMappingHandlerAdapter pair which are the default in the MVC Java config and the MVC namespace.
            @RestController는 MVCJava 구성 및 MVC 네임스페이스의 기본값인 RequestMappingHandlerMapping-RequestMappingHandlerAdapter 쌍과 같이 적절한 Handler-HandlerAdapter 쌍이 구성된 경우 처리됩니다.*
            > 
        - ***@ResponseBody***
            
            > *Annotation that indicates a method return value should be bound to the web response body. Supported for annotated handler methods.
            메서드 반환 값을 나타내는 어노테이션으로써 웹 응답 본문에 메소드의 리턴값들이 바인딩되어야 합니다. 
            
            As of version 4.0 this annotation can also be added on the type level in which case it is inherited and does not need to be added on the method level.
            4.0부터는 이 어노테이션은 타입 레벨에서 추가될 수 있으며, 상속이 가능합니다.*
            > 
- HTTP 요청 URL 경로에 있는 값을 가져오고 싶을 땐 ***@PathVariable***을 사용하자.
    - ***@PathVariable***
        
        > *Annotation which indicates that a method parameter should be bound to a URI template variable. Supported for RequestMapping annotated handler methods.
        이 어노테이션은 메서드 파라미터가 URI의 템플릿에 반드시 바인딩되어야 한다는 것을 나타냅니다.
        
        If the method parameter is Map<String, String> then the map is populated with all path variable names and values.
        만약 메서드 인자가 Map<String, String>이라면 이 map은 모든 경로 변수 이름과 값으로 채워집니다.*
        > 
- HTTP요청 URL을 통해 넘어오는 쿼리 스트링(요청 파라미터) 혹은 POST 요청의 HTML Form 데이터를 가져오고 싶다면 ***@RequestParam***을 사용하자.
    - ***@RequestParam***
        
        > *Annotation which indicates that a method parameter should be bound to a web request parameter.
        메서드 매개 변수가 웹 요청 매개 변수에 바인딩되어야 함을 나타내는 주석입니다.
        
        Supported for annotated handler methods in Spring MVC and Spring WebFlux as follows:
        Spring MVC 및 Spring WebFlux에서 매핑된 핸들러의 경우 지원됩니다.
        
        - In Spring MVC, "request parameters" map to query parameters, form data, and parts in multipart requests. This is because the Servlet API combines query parameters and form data into a single map called "parameters", and that includes automatic parsing of the request body.
        Spring MVC에서 "요청 매개변수"는 쿼리 스트링, Form 데이터, 멀티파트 데이터에 매핑됩니다. 이는 서블릿 API가 쿼리 매개 변수와 Form 데이터를 "파라미터"라고 불리는 단일 맵으로 결합하고 요청 본문의 자동 구문 분석을 포함하기 때문입니다.
        
        - In Spring WebFlux, "request parameters" map to query parameters only. To work with all 3, query, form data, and multipart data, you can use data binding to a command object annotated with ModelAttribute.
        Spring WebFlux에서 "요청 매개 변수"는 쿼리 스트링에만 매핑됩니다. 쿼리 스트링, Form 데이터 및 멀티파트 데이터 3개를 모두 사용하려면 @ModelAttribute 어노테이션이 매핑된 커맨드 객체에 대해서만 가능합니다.
        → 커맨드 객체라고 부르는 이유는 
        
        - If the method parameter type is Map and a request parameter name is specified, then the request parameter value is converted to a Map assuming an appropriate conversion strategy is available.
        메서드 매개 변수 유형이 Map이고 요청 매개 변수 이름이 지정된 경우 적절한 변환 전략을 사용할 수 있다고 가정하면 요청 매개 변수 값이 Map으로 변환됩니다.
        
        - If the method parameter is Map<String, String> or MultiValueMap<String, String> and a parameter name is not specified, then the map parameter is populated with all request parameter names and values.
        메서드 매개 변수가 Map<String, String> 또는 MultiValueMap<String, String>이고 매개 변수 이름이 지정되지 않은 경우 맵 매개 변수는 모든 요청 매개 변수 이름과 값으로 채워집니다.
        
        Since: 2.5*
        > 
- 우리는 앞서 요청 파라미터를 ***@RequsetParam***을 통해 매핑했다. 하지만 이 경우 변수가 많아지는 경우 코드 가독성과 유지보수가 어려워진다. 따라서 이를 해결하기 위해 **커맨드 객체**라는 개념이 등장했다. 일반적인 경우는 DTO를 의미하며, 우리는 요청 파라미터를 커맨드 객체(DTO)에 매핑하는 것을 통해 복잡성을 해결할 수 있다. 이럴 경우 ***@ModelAttribute***를 사용하자.
    - ***@ModelAttribute***는 메서드 레벨 혹은 메서드 인자에 사용될 수 있다. 위에서 말했듯 이 어노테이션의 기능은 **요청 파라미터를 커맨드 객체에 바인딩시켜주는 것**이다. 그리고 스프링에서는 위 어노테이션을 인자의 경우 생략해도 바인딩을 가능케 준다.
        - 동작 로직: 인자로 명시된 객체를 생성 → 요청 파라미터의 이름과 객체의 프로퍼티를 매칭 → 이 단계에서 유효성 검증 → 만약 있다면 setter 메서드를 사용해 바인딩
        - 스프링은 내부적으로 요청 파라미터가 복잡한 경우 커맨드 객체로 매핑해주고, 이외의 String, int 등의 자료형은 ***@RequsetParam***으로 취급해 바인딩해준다.
        - 또한 바인딩하는 과정에서 ***@RequsetParam***과는 달리 Validation 작업을 내부적으로 진행해줍니다. 바인딩 실패 시 `org.springframework.validation.BindException` 예외가 발생한다.
    - ***@ModelAttribute***
        
        > *Annotation that binds a method parameter or method return value to a named model attribute, exposed to a web view. Supported for controller classes with @RequestMapping methods.
        이 어노테이션은 메서드 인자 혹은 메서드 반환 값을 Web View에 노출되는 model attribute에 바인딩합니다. @RequestMapping이 명시된 컨트롤러 클래스의 메서드에 대해 지원됩니다.
        
        Can be used to expose command objects to a web view, using specific attribute names, through annotating corresponding parameters of an @RequestMapping method.
        @RequestMapping 메서드의 매개 변수에 주석을 달아 특정 속성 이름을 사용하여 명령 객체를 웹 보기에 노출하는 데 사용할 수 있습니다.
        
        Can also be used to expose reference data to a web view through annotating accessor methods in a controller class with @RequestMapping methods. Such accessor methods are allowed to have any arguments that @RequestMapping methods support, returning the model attribute value to expose.
        @RequestMapping 메서드를 사용하여 컨트롤러 클래스의 접근자 메서드에 주석을 달아 참조 데이터를 웹 뷰에 노출하는 데도 사용할 수 있습니다. 이러한 접근자 메서드는 @RequestMapping 메서드가 지원하는 인수를 가질 수 있으므로 모델 속성 값을 반환하여 노출할 수 있습니다.
        
        Note however that reference data and all other model content is not available to web views when request processing results in an Exception since the exception could be raised at any time making the content of the model unreliable. For this reason @ExceptionHandler methods do not provide access to a Model argument.
        그러나 모델 내용을 신뢰할 수 없게 만들 수 있는 예외가 언제든지 발생할 수 있으므로 요청 처리 결과 예외가 발생할 경우 참조 데이터와 다른 모든 모델 콘텐츠를 웹 뷰에서 사용할 수 없습니다. 이러한 이유로 @ExceptionHandler 메서드는 Model 인수에 대한 액세스를 제공하지 않습니다.
        
        Since: 2.5*
        > 
- **HttpMessageConverter**
    - JSP와 같은 뷰 페이지를 반환하지 않고 HttpBody에 값을 직접 반환하는 경우 Spring은 ViewResolver 대신 HttpMessageConverter가 동작한다.
        - 이 때 Http 메시지에 담긴 미디어 타입과 어노테이션이 매핑된 인자의 클래스 타입의 적합성 여부를 확인하고 순차적으로 메시지 컨버터를 탐색하는 식으로 동작한다.
        - 문자를 처리하는 경우는 StringHttpMessageConverter
        - 객체를 매핑하는 경우는 MappingJackson2HttpMessageConverter
    - Http 요청인 경우 @RequestBody, HttpEntity(RequestEntity)이 사용된 경우, 응답인 경우 @ResponseBody, HttpEntity(ResponseEntity)가 사용된 경우 HttpMessageConverter가 호출된다.
    - 생각해보면, 애노테이션 기반의 컨트롤러는 다양한 파라미터를 사용할 수 있다. HttpServletRequest, Model, @RequestParam , @ModelAttribute, @RequestBody , HttpEntity까지 다양한 파라미터가 들어갈 수 있다. 넘어오는 인자가 아주 많다. 그런데 스프링은 어떻게 알고 각 어노테이션과 인자에 맞는 동작을 하는 것일까?
        - 바로 RequestMappingHandlerAdapter가 호출하는 ArgumentResolver 때문이다.
        - HandlerAdapter가 내부적으로 ArgumentResolver의 supportsParameter()를 호출해 컨트롤러의 파를미라터를 원할수  있는지 확인한다.
        - 그리고 ArgumentResolver가 파라미터에 해당되는 값들을 생성해 반환해주면 HandlerAdapter가 핸들러 메서드를 생성할 때 인자들을 넣어준다.