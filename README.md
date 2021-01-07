# 아두이노를 이용한 스마트 난방 시스템
> 아두이노를 스마트 난방 시스템이며, 안드로이드 어플을 이용하여 원격 조종을 할 수 있습니다.
- 이 저장소는 Android(Java)코드만 포함하고 있습니다.
- 서버와 연동하여 어플이 작동하는 것을 보여드리기 위해 어플 사용 화면이 다소 복잡할 수 있습니다.
- 3개의 화면으로 구성되어 각각 온도 값 설정, 온도 값 도식화, 기기 설정을 담당합니다.
- 아두이노 기기 등록 및 전환, 기기 삭제, 희망 온도 값 설정, 온도 값 도식화, 비정상 온도 발생 시 알림을 지원합니다.
![스크린샷 2021-01-07 오후 11 37 24](https://user-images.githubusercontent.com/48707020/103905681-44450f00-5142-11eb-8d4e-4e3137b30592.png)

## 목차  
[- 아두이노 등록 및 전환](#아두이노-등록-및-전환)  
[- 아두이노 삭제](#아두이노-삭제)  
[- 희망 온도 값 설정](#온도값-설정)  
[- 온도 값 도식화](#온도값-도식화)  
[- 비정상 온도 발생 시 알림](#비정상-온도-발생-시-알림)  

### 아두이노 등록 및 전환  
![아두이노 등록 및 전환](https://user-images.githubusercontent.com/48707020/103151810-c89fa580-47c4-11eb-99d0-0743d94f6307.gif)  
아두이노 기기를 등록하는 기능과 전환하는 기능입니다.  
기기를 새로 등록하면 현재 사용하는 기기가 새로 등록한 기기로 바뀝니다.  
다른 기기로 전환하고 싶을 때는 스위치 버튼만 누르면 사용하는 기기를 전환할 수 있습니다.  

### 아두이노 삭제  
![아두이노 삭제](https://user-images.githubusercontent.com/48707020/103151814-d35a3a80-47c4-11eb-905d-ed0580f17064.gif)  
현재 사용자에게 등록되어 있는 기기를 삭제하고자 할 때 사용합니다. 만약, 지금 사용하고 있는 기기를 삭제할 시 바로 기기를 선택하거나 등록할 수 있습니다.  

### 온도값 설정  
![안드로이드 - 온도값 설정](https://user-images.githubusercontent.com/48707020/103151817-d5bc9480-47c4-11eb-80d2-bafa545a7c3b.gif)  
- 기기에 희망하는 온도 값을 전송합니다.  
- Seekbar를 custom하여 설정 하려는 값이 몇 도인지 쉽게 확인할 수 있습니다.  
- 온도 값마다 이미지와 색이 변경되도록 하여 어느 정도로 온도 값이 변경되는 지 쉽게 파악할 수 있습니다.  
Seekbar의 thumb를 custom하는 코드입니다. thumbView는 R.layout.layout_seekbar_thumb를 통해 inflate 됩니다.   
<pre>
<code>
private Drawable getThumb(int progress) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }
</code>
</pre>

Seekbar의 thumb를 custom하는 코드를 통해 Seekbar의 thumb를 설정해줍니다. listener를 통해 seekbar의 thumb 안의 textview를 조정해줍니다.
<pre>
<code>
seekBar.setThumb(getThumb(21));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(thumbDrawable);
                hopeTemp.setText(String.valueOf(progress+16));
                changeBitmap(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setThumb(thumbDrawable);
                hopeTemp.setText(String.valueOf(seekBar.getProgress()+16));
                showReveal(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setThumb(getThumb(seekBar.getProgress() + 16));
                showReveal(false);
            }
        });
</code>
</pre>

### 온도값 도식화  
![안드로이드-온도값 도식화](https://user-images.githubusercontent.com/48707020/103151818-d7865800-47c4-11eb-94dd-e239b68dc38e.gif)  
기기의 온도 history를 그래프로 생성하고, 밑으로 스크롤하면 온도값들을 시간대별로 파악할 수 있습니다.  

### 비정상 온도 발생 시 알림  
![알림](https://user-images.githubusercontent.com/48707020/103151819-d9501b80-47c4-11eb-95a9-f04c72595abb.gif)  
만약, 기기의 온도 값이 비정상인 값으로 검출되면 Notification이 백그라운드에서 오게 됩니다.  
Notification을 클릭하면 해당하는 기기의 메인 화면으로 이동합니다.  
