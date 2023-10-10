# registerForActivityResult 메서드
는 Android에서 Activity Result API를 사용하여 결과를 처리하기 위한 ActivitiyResultLauncher를 생성할 때 사용된다.
이 메서드는 두 개의 인자를 받는다.
1. Contract(계약) : 첫 번째 인자로는 사용할 Contract(계약)가 들어간다. Contract는 액티비티나 프래그먼트에서 다른 액티비티나 외부 컴포넌트로부터 결과를 받는 방법을 정의한다. ActivityResultContracts 클래스에는 여러 가지 Contract가 정의되어 있으며, 예를 들어 권한 요청, 카메라로부터 이미지 가져오기, 갤러리로부터 이미지 가져오기 등 다양한 상황에서 사용할 수 있다.
2. Callback(콜백) : 두 번째 인자로는 결과를 처리할 콜백 함수(람다 표현식)가 들어간다. 이 콜백 함수는 Contract에 정의된 결과를 처리하는 역할을 한다. 결과가 발생했을 때 이 콜백 함수가 실행된다.

        예를 들어, 권한 요청 결과를 처리하기 위해서는 다음과 같이 ActivityResultContracts.RequestMultiplePermissions()를 Contract로 사용하고, 람다 표현식을 콜백으로 전달한다.

## result.getOrDefault(key, defaultValue)
는 Java의 Map 인터페이스의 메서드 중 하나이다. 이 메서드는 주어진 키(key)로 맵에서 값을 가져오거나, 키가 없을 때 기본값을 반환하는 역할을 한다. 여기에서 result는 ActivityResultLauncher의 콜백에서 반환된 ActivityResult 객체이다. 이 객체는 권한 요청 결과를 포함하고 있으며, 각 권한에 대한 부여 여부를 나타내는 정보를 가지고 있다.

* key : 가져올 값을 가리키는 키이다. 이 경우, 권한 이름을 나타내는 문자열이 된다. 예를 들어 Manifest.permission.ACCESS_FINE_LOCATION 또는 Manifest.permission.ACCESS_COARSE_LOCATION과 같은 값이 키가 될 수 있다.

* defaultValue : 키가 맵에 없거나 null인 경우 반환할 기본값이다. 이 값은 키가 존재하지 않을 떄 반환되며, 키가 존재하고 값이 null이 아닌 경우에는 해당 값이 반환된다.

따라서 주어진 코드에서는 result 객체에서 자세한 위치 권한 ACCESS_FINE_LOCATION과 대략적인 위치 권한 ACCESS_COARSE_LOCATION의 부여 여부를 확인하는 데 사용된다. 만약 해당 권한에 대한 정보가 없거나 null인 경우 false를 기본값으로 반환한다. 그렇게 얻어진 값을 fineLocationGranted와 coarseLocationGranted 변수에 저장하여 후속 작업에 사용된다.
