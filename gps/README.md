# registerForActivityResult 메서드
는 Android에서 Activity Result API를 사용하여 결과를 처리하기 위한 ActivitiyResultLauncher를 생성할 때 사용된다.
이 메서드는 두 개의 인자를 받는다.
1. Contract(계약) : 첫 번째 인자로는 사용할 Contract(계약)가 들어간다. Contract는 액티비티나 프래그먼트에서 다른 액티비티나 외부 컴포넌트로부터 결과를 받는 방법을 정의한다. ActivityResultContracts 클래스에는 여러 가지 Contract가 정의되어 있으며, 예를 들어 권한 요청, 카메라로부터 이미지 가져오기, 갤러리로부터 이미지 가져오기 등 다양한 상황에서 사용할 수 있다.
2. Callback(콜백) : 두 번째 인자로는 결과를 처리할 콜백 함수(람다 표현식)가 들어간다. 이 콜백 함수는 Contract에 정의된 결과를 처리하는 역할을 한다. 결과가 발생했을 때 이 콜백 함수가 실행된다.
    예를 들어, 
