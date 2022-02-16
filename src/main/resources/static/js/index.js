
$(window).on('load', function () {
    $("#register").on('click', () => registerButtonClicked());
    $("#authenticate").on('click', () => authenticateButtonClicked());


});

const abortController = new AbortController();
const abortSignal = abortController.signal;


function registerButtonClicked() {
    let username = $("input[name='username']").val();
    let displayName = $("input[name='username']").val();
    if (username === "") {
        $("#status").text("Input user name first");
        $("#status").removeClass('hidden');
    }

    disableControls();
    $("#registerSpinner").removeClass("hidden");

    let serverPublicKeyCredentialCreationOptionsRequest = {
        username: username,
        displayName: displayName
    };

    getRegChallenge(serverPublicKeyCredentialCreationOptionsRequest)
        .then(createCredentialOptions => {
            return createCredential(createCredentialOptions);
        })
        .then(() => {
            $("#status").text("Successfully created credential");
            $("#registerSpinner").addClass("hidden");
            enableControls();
        })
        .catch(e => {
            $("#status").text("Error: " + e);
            $("#registerSpinner").addClass("hidden");
            enableControls();
        });

}

function authenticateButtonClicked() {
    disableControls();
    $("#authenticateSpinner").removeClass("hidden");

    let username  = $("input[name='username']").val();
    // let userVerification = $("input[name='userVerificationRequired']:checked").val();

    // prepare parameter
    let serverPublicKeyCredentialGetOptionsRequest = {
        username: username,
        // userVerification: userVerification
    };

    getAuthChallenge(serverPublicKeyCredentialGetOptionsRequest)
        .then(getCredentialOptions => {
            return getAssertion(getCredentialOptions);
        })
        .then(() => {
            $("#status").text("Successfully verified credential");
            $("#authenticateSpinner").addClass("hidden");
            enableControls()
        }).catch(e => {
        $("#status").text("Error: " + e);
        $("#authenticateSpinner").addClass("hidden");
        $("#status").removeClass('hidden');
        enableControls()
    });
}

function getAuthChallenge(serverPublicKeyCredentialGetOptionsRequest) {
    logObject("Get auth challenge", serverPublicKeyCredentialGetOptionsRequest);
    return rest_post("/rp/assertion/options", serverPublicKeyCredentialGetOptionsRequest)
        .then(response => {
            logObject("Get auth challenge", response);
            if (response.status !== 'ok') {
                return Promise.reject(response.errorMessage);
            } else {
                let getCredentialOptions = performGetCredReq(response);
                return Promise.resolve(getCredentialOptions);
            }
        });
}

function getAssertion(options) {
    if (!PublicKeyCredential) {
        return Promise.reject("WebAuthn APIs are not available on this user agent.");
    }

    return navigator.credentials.get({publicKey: options, signal: abortSignal})
        .then(rawAssertion => {
            logObject("raw assertion", rawAssertion);

            let assertion = {
                rawId: base64UrlEncode(rawAssertion.rawId),
                id: base64UrlEncode(rawAssertion.rawId),
                response: {
                    clientDataJSON: base64UrlEncode(rawAssertion.response.clientDataJSON),
                    userHandle: base64UrlEncode(rawAssertion.response.userHandle),
                    signature: base64UrlEncode(rawAssertion.response.signature),
                    authenticatorData: base64UrlEncode(rawAssertion.response.authenticatorData)
                },
                type: rawAssertion.type,
            };

            if (rawAssertion.getClientExtensionResults) {
                assertion.extensions = rawAssertion.getClientExtensionResults();
            }

            console.log("=== Assertion response ===");
            logVariable("rawId (b64url)", assertion.rawId);
            logVariable("id (b64url)", assertion.id);
            logVariable("response.userHandle (b64url)", assertion.response.userHandle);
            logVariable("response.authenticatorData (b64url)", assertion.response.authenticatorData);
            logVariable("response.lientDataJSON", assertion.response.clientDataJSON);
            logVariable("response.signature (b64url)", assertion.response.signature);
            logVariable("id", assertion.type);

            return rest_post("/rp/assertion/result", assertion);
        })
        .catch(function(error) {
            logVariable("get assertion error", error);
            if (error == "AbortError") {
                console.info("Aborted by user");
            }
            return Promise.reject(error);
        })
        .then(response => {
            if (response.status !== 'ok') {
                return Promise.reject(response.errorMessage);
            } else {
                return Promise.resolve(response);
            }
        });
}

let performGetCredReq = (getCredReq) => {
    getCredReq.challenge = base64UrlDecode(getCredReq.challenge);

    //Base64url decoding of id in allowCredentials
    if (getCredReq.allowCredentials instanceof Array) {
        for (let i of getCredReq.allowCredentials) {
            if ('id' in i) {
                i.id = base64UrlDecode(i.id);
            }
        }
    }

    delete getCredReq.status;
    delete getCredReq.errorMessage;

    removeEmpty(getCredReq);

    logObject("Updating credentials ", getCredReq)
    return getCredReq;
}


function disableControls() {
    $('#register').attr('disabled','');
    $('#authenticate').attr('disabled','');
    $("#status").addClass('hidden');
}

function enableControls() {
    $('#register').removeAttr('disabled');
    $('#authenticate').removeAttr('disabled');
    $("#status").removeClass('hidden');
}

function getRegChallenge(serverPublicKeyCredentialCreationOptionsRequest) {
    logObject("Get reg challenge", serverPublicKeyCredentialCreationOptionsRequest);
    return rest_post("/rp/attestation/options", serverPublicKeyCredentialCreationOptionsRequest)
        .then(response => {
            logObject("Get reg challenge response", response);
            if (response.status !== 'ok') {
                console.log("=== not ok")
                return Promise.reject(response.errorMessage);
            } else {
                console.log("=== ok")
                let createCredentialOptions = performMakeCredReq(response);
                return Promise.resolve(createCredentialOptions);
            }
        });
}

function createCredential(options) {
    if (!PublicKeyCredential || typeof PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable !== "function") {
        return Promise.reject("WebAuthn APIs are not available on this user agent.");
    }

    return navigator.credentials.create({publicKey: options, signal: abortSignal})
        .then(rawAttestation => {
            logObject("raw attestation", rawAttestation);
            let attestation = {
                rawId: base64UrlEncode(rawAttestation.rawId),
                id: base64UrlEncode(rawAttestation.rawId),
                response : {
                    clientDataJSON: base64UrlEncode(rawAttestation.response.clientDataJSON),
                    attestationObject: base64UrlEncode(rawAttestation.response.attestationObject)
                },
                type: rawAttestation.type,
            };

            if (rawAttestation.getClientExtensionResults) {
                attestation.extensions = rawAttestation.getClientExtensionResults();
            }

            // set transports if it is available
            if (typeof rawAttestation.response.getTransports === "function") {
                attestation.response.transports = rawAttestation.response.getTransports();
            }

            console.log("=== Attestation response ===");
            logVariable("rawId (b64url)", attestation.rawId)
            logVariable("id (b64url)", attestation.id);
            logVariable("response.clientDataJSON (b64url)", attestation.response.clientDataJSON);
            logVariable("response.attestationObject (b64url)", attestation.response.attestationObject);
            logVariable("response.transports", attestation.response.transports);
            logVariable("id", attestation.type);

            return rest_post("/rp/attestation/result", attestation);
        })
        .catch(function(error) {
            logVariable("create credential error", error);
            if (error == "AbortError") {
                console.info("Aborted by user");
            }
            return Promise.reject(error);
        })
        .then(response => {
            if (response.status !== 'ok') {
                return Promise.reject(response.errorMessage);
            } else {
                return Promise.resolve(response);
            }
        });
}

function logObject(name, object) {
    console.log(name + ": " + JSON.stringify(object));
}

function logVariable(name, text) {
    console.log(name + ": " + text);
}

function base64UrlEncode(arrayBuffer) {
    if (!arrayBuffer || arrayBuffer.length == 0) {
        return undefined;
    }

    return btoa(String.fromCharCode.apply(null, new Uint8Array(arrayBuffer)))
        .replace(/=/g, "")
        .replace(/\+/g, "-")
        .replace(/\//g, "_");
}



function rest_post(endpoint, object) {
    return fetch(endpoint, {
        method: "POST",
        credentials: "same-origin",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json"
        }
    })
        .then(response => {
            return response.json();
        });
}


let performMakeCredReq = (makeCredReq) => {
    makeCredReq.challenge = base64UrlDecode(makeCredReq.challenge);
    makeCredReq.user.id = base64UrlDecode(makeCredReq.user.id);

    //Base64url decoding of id in excludeCredentials
    if (makeCredReq.excludeCredentials instanceof Array) {
        for (let i of makeCredReq.excludeCredentials) {
            if ('id' in i) {
                i.id = base64UrlDecode(i.id);
            }
        }
    }

    delete makeCredReq.status;
    delete makeCredReq.errorMessage;
    // delete makeCredReq.authenticatorSelection;

    removeEmpty(makeCredReq);

    logObject("Updating credentials ", makeCredReq)
    return makeCredReq;
}

function removeEmpty(obj) {
    for (let key in obj) {
        if (obj[key] == null || obj[key] === "") {
            delete obj[key];
        } else if (typeof obj[key] === 'object') {
            removeEmpty(obj[key]);
        }
    }
}

/**
 * Base64 url encodes an array buffer
 * @param {ArrayBuffer} arrayBuffer
 */
function base64UrlEncode(arrayBuffer) {
    if (!arrayBuffer || arrayBuffer.length == 0) {
        return undefined;
    }

    return btoa(String.fromCharCode.apply(null, new Uint8Array(arrayBuffer)))
        .replace(/=/g, "")
        .replace(/\+/g, "-")
        .replace(/\//g, "_");
}

/**
 * Base64 url decode
 * @param {String} base64url
 */
function base64UrlDecode(base64url) {
    let input = base64url
        .replace(/-/g, "+")
        .replace(/_/g, "/");
    let diff = input.length % 4;
    if (!diff) {
        while(diff) {
            input += '=';
            diff--;
        }
    }

    return Uint8Array.from(atob(input), c => c.charCodeAt(0));
}
