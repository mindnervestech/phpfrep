function sponser(radio) {
	if(radio.value == 0)
	{
		document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'block';
		document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'block';
	}
	else
	{
		document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'none';
		document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'none';
		
	}
}

function prevParticipation(radioOptions){
if(radioOptions.value == 0)
{
	document.getElementById('studentProficiencyInfo:sponsor').style.display = 'none';
	document.getElementById('studentProficiencyInfo:sponsorRadioLabel').style.display = 'none';
	document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'none';
	document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'none';
}
else
{
	document.getElementById('studentProficiencyInfo:sponsor').style.display = 'block';
	document.getElementById('studentProficiencyInfo:sponsorRadioLabel').style.display = 'block';
	document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'block';
	document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'block';
}
}
function workExperience(workExperienceRadio){
	if(workExperienceRadio.value == 0)
	{
		document.getElementById('studentProficiencyInfo:yearOfExperienceLabel').style.display = 'none';
		document.getElementById('studentProficiencyInfo:yearOfExperience').style.display = 'none';
	}
	else
	{
		document.getElementById('studentProficiencyInfo:yearOfExperienceLabel').style.display = 'block';
		document.getElementById('studentProficiencyInfo:yearOfExperience').style.display = 'block';
	}
	}
function sponser1() {
	
	var radio = document.getElementById('studentProficiencyInfo:hiddenSponser');
	var radioOptions = document.getElementById('studentProficiencyInfo:hiddenPrevParticipation');	
	var workExperienceRadio = document.getElementById('studentProficiencyInfo:hiddenWorkExperience');
	
	if(workExperienceRadio.value == 0)
	{
		document.getElementById('studentProficiencyInfo:yearOfExperienceLabel').style.display = 'none';
		document.getElementById('studentProficiencyInfo:yearOfExperience').style.display = 'none';
	}
	else
	{
		document.getElementById('studentProficiencyInfo:yearOfExperienceLabel').style.display = 'block';
		document.getElementById('studentProficiencyInfo:yearOfExperience').style.display = 'block';
	}
	if(radioOptions.value == 0)
	{
		document.getElementById('studentProficiencyInfo:sponsor').style.display = 'none';
		document.getElementById('studentProficiencyInfo:sponsorRadioLabel').style.display = 'none';
		document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'none';
		document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'none';
	}
	else
	{
		document.getElementById('studentProficiencyInfo:sponsor').style.display = 'block';
		document.getElementById('studentProficiencyInfo:sponsorRadioLabel').style.display = 'block';
		document.getElementById('studentProficiencyInfo:sponsorName').style.display = 'block';
		document.getElementById('studentProficiencyInfo:sponsorNameLabel').style.display = 'block';
	}
	
}

function countryDetails(val)
{
	var countryOfBirth = val.value;
	document.getElementById('studentPrimaryInfo:countryOfCitizenship').value =countryOfBirth;
	if(countryOfBirth == document.getElementById('studentPrimaryInfo:countryOfCitizenship').value)
		document.getElementById('studentPrimaryInfo:nationality').value = countryOfBirth;
	else
		document.getElementById('studentPrimaryInfo:nationality').value = document.getElementById('studentPrimaryInfo:countryOfCitizenship').value;
}

function enable(id,id1,id2) {
	var value= document.getElementById(id);
	var element2 = document.getElementById(id2);
	// Show it.
	if(value.checked)
	{
		element2.disabled = ''; 
	}
	else
	{
		element2.disabled = 'true'; 
	}
	
	}
//Phone number format
function mask(inputName, mask, evt) {
    try {
      var text = document.getElementById(inputName);
      var value = text.value;
      // If user pressed DEL or BACK SPACE, clean the value
     /* try {
        var e = (evt.which) ? evt.which : event.keyCode;
        if ( e == 46 || e == 8 ) {
          text.value = "";
          return;
        }
      } catch (e1) {}*/
 
      var literalPattern=/[0\*]/;
      var numberPattern=/[0-9]/;
      var newValue = "";
      for (var vId = 0, mId = 0 ; mId < mask.length ; ) {
        if (mId >= value.length)
          break;
        // Number expected but got a different value, store only the valid portion
        if (mask[mId] == '0' && value[vId].match(numberPattern) == null) {
          break;
        }
        // Found a literal
        while (mask[mId].match(literalPattern) == null) {
          if (value[vId] == mask[mId])
            break;
        newValue += mask[mId++];
      }
      newValue += value[vId++];
      mId++;
    }
    text.value = newValue;
  } catch(e) {}
}
function verification()
{
if(confirm('Are you sure want to delete?'))
	return true; 
else 
	return false;
}
