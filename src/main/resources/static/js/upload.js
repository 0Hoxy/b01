async function uploadToServer (formObj){
    console.log("업로드 to 서버....")
    //console.log(formObj)

    const response = await axios({
        method: 'post',
        url: '/upload',
        data: formObj,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })

    return response.data
}

async function removeFileToServer(uuid, fileName) {
    const response = await axios.delete(`/remove/${uuid}_${fileName}`)
    return response.data
}