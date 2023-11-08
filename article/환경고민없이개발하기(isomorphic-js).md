# 환경 고민없이 개발하기 (isomorphic)
- https://toss.tech/article/isomorphic-javascript

## SSR
- 렌더링 작업 일부를 서버에 위임하는 방식으로, 브라우저에 완성된 HTML을 전달
- 서비스는 SEO 최적화를 통해 더 많은 노출 기회를 얻을 수 있다.
- 하지만 SSR을 위해선 별도의 서버 운영이 필요하다.
- 렌더링 과정에서 서버가 개입되다보니 location is not defined과 같은 에러 등을 볼 수 있다.

### Nest.js 렌더링 과정 중 발생 에러들
1. location is not defined
```js
function App() {
	// 쿼리 파라미터로 전달받은 유저의 이름을 얻어온다.
	const name = new URL(location.href).searchParams.get(name);	
	// 유저의 이름을 화면에 출력한다.
	return <div>{name}</div>
}
```
- 위 코드가 SSR 환경에서는 에러가 발생한다. 브라우저 객체인 location이 존재하지 않기 때문


2. Hydration Mismatch
- Text content does not match server-rendered HTML.
- React는 이벤트 리스너, 상태 관리와 같은 클라이언트 로직을 전달받은 HTML과 통합하여 작동한다.
  - 이 과정을 `Hydration`이라고 한다.
- 즉 React는 element와 가상 DOM을 생성한 뒤 전달받은 HTML과 비교하기 때문에 서버와 클라이언트의 렌더링 결과가 같은 경우에만 `Hydration`을 수행한다.
- 서버에서 생성한 HTML은 단순 마크업이므로 파라미터가 존재하는 경우 서버와 클라이언트는 각각 다른 결과물을 렌더링하게 되어 `Hydration`을 수행할 수 없다.

## 해결방법
- Hydration Mismatch를 해결하기 위해선 단순하게 서버와 클라이언트의 렌더링 결과가 같으면 된다.
- 이를 위해 서버 환경에서 쿼리 파라미터에 접근할 수 있는 별도의 로직 작성이 필요
```js
function App() {
	const name = (() => {
		if (isServer()) {
	    /* 서버 환경에서 쿼리 파라미터 접근 및 반환하는 별도 로직... */
		}
		 return new URL(location.href).searchParams.get(name);	
	});
	return <div>{name}</div>
}
```
- Next.js는 `useRouter()` 를 통해 서버, 클라이언트 어떤 환경에서든 동일한 결과 값을 보장받을 수 있도록 지원한다.
```js
import { useRouter } from 'next/router';

function App() {
	const name = useRouter().query.name;

	return <div>{name}</div>
}
```
- 이처럼 서버와 클라이언트 양측에 동일한 결과를 보장하는 코드를 `isomorphic`하다고 표현한다.
