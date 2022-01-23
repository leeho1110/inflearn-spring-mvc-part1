
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

---

**`@RequestMapping`**

보통 요청에 사용될 URL을 위 어노테이션의 value값으로 입력한다. `RequestMappingHandlerMapping`은 스프링 빈 중에서
`@RequestMapping` 또는 `@Controller`가 클래스 레벨에 붙어있는 경우 매핑 정보로 인식한다.

---

**`@ResponseBody`**

1. HTTP Body에 문자 내용 직접 변환
2. `viewResolver` 대신 `HttpMessageConverter` 동작
3. 기본 문자 처리 : `StringHttpMessageConverter`
4. 기본 객체 처리 : `MappingJackson2HttpMessageConverter`
5. byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어있음
> 응답의 경우 클라이언트의 Http Accept 헤더와 서버의 컨트롤러 반환 타입 정보를 조합하여 `HttpMessageConverter`
> 가 선택된다. 

---

**`org.springframework.http.converter.HttpMessageConverter`**

HTTP 메시지 컨버터는 HTTP 요청, 응답 둘다 사용된다.

- `canRead()`, `canWrite()` : 메시지 컨버터가 해당 클래스, 미디어 타입을 지원하는지 확인
- `read()`, `write()` : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능

스프링 부트는 이 외에도 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 둘을 체크해서 컨버터 종류를 결정한다.
메시지 컨버터를 순회 탐색하며 조건에 만족하는 컨버터를 탐색한다.

- `ByteArrayHttpMessageConverter` : byte 데이터 처리
  - 클래스 타입 : `byte[]` , 미디어 타입 : `*/*`
  - 요청 예시: `@RequestBody byte[] data`
  - 응답 예시 : `@ResponseBody return byte[]`, HTTP 응답 미디어 타입: `application/octet-stream`
  
- `StringHttpMessageConverter` : String 문자 데이터 처리
  - 클래스 타입 : `String`, 미디어 타입: `*/*`
  - 요청 예시 : `@RequestBody String data`
  - 응답 예시 : `@ResponseBody return "ok"`, HTTP 응답 미디어 타입 : `text/plain`
  
- `MappingJackson2HttpMessageConverter` : Json 데이터 처리
  - 클래스 타입: 객체 또는 HashMap, 미디어 타입: `application/json`
  - 요청 예시 : `@RequestBody HelloData data`
  - 응답 예시 : `@ResponseBody return helloData`, HTTP 응답 미디어 타입 : `application/json`