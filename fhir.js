import jsonFile from './bundle.json';


export const postFhirData = async() => {

    const rawResponse = await fetch('http://hapi.fhir.org/baseR4/', {
        method: 'POST',
         headers: {
                    'Access-Control-Allow-Origin': '*',
                     'Accept': 'application/json',
                     'Content-Type': 'application/json',
                     'Access-Control-Allow-Methods': 'PUT, GET, POST, DELETE, OPTIONS'
                      },
          body:  JSON.stringify(jsonFile)   
  })
  const content = await rawResponse.json();
                  console.log("fhir:",content);

}