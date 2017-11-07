/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext = {
	version : "2.0.2"
};
window["undefined"] = window["undefined"];
Ext.apply = function(C, D, B) {
	if (B) {
		Ext.apply(C, B)
	}
	if (C && D && typeof D == "object") {
		for ( var A in D) {
			C[A] = D[A]
		}
	}
	return C
};
(function() {
	var idSeed = 0;
	var ua = navigator.userAgent.toLowerCase();
	var isStrict = document.compatMode == "CSS1Compat", isOpera = ua
			.indexOf("opera") > -1, isSafari = (/webkit|khtml/).test(ua), isSafari3 = isSafari
			&& ua.indexOf("webkit/5") != -1, isIE = !isOpera
			&& ua.indexOf("msie") > -1, isIE7 = !isOpera
			&& ua.indexOf("msie 7") > -1, isGecko = !isSafari
			&& ua.indexOf("gecko") > -1, isBorderBox = isIE && !isStrict, isWindows = (ua
			.indexOf("windows") != -1 || ua.indexOf("win32") != -1), isMac = (ua
			.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1), isAir = (ua
			.indexOf("adobeair") != -1), isLinux = (ua.indexOf("linux") != -1), isSecure = window.location.href
			.toLowerCase().indexOf("https") === 0;
	if (isIE && !isIE7) {
		try {
			document.execCommand("BackgroundImageCache", false, true)
		} catch (e) {
		}
	}
	Ext
			.apply(
					Ext,
					{
						isStrict : isStrict,
						isSecure : isSecure,
						isReady : false,
						enableGarbageCollector : true,
						enableListenerCollection : false,
						SSL_SECURE_URL : "javascript:false",
						BLANK_IMAGE_URL : "",
						emptyFn : function() {
						},
						applyIf : function(o, c) {
							if (o && c) {
								for ( var p in c) {
									if (typeof o[p] == "undefined") {
										o[p] = c[p]
									}
								}
							}
							return o
						},
						addBehaviors : function(o) {
							if (!Ext.isReady) {
								Ext.onReady(function() {
									Ext.addBehaviors(o)
								});
								return
							}
							var cache = {};
							for ( var b in o) {
								var parts = b.split("@");
								if (parts[1]) {
									var s = parts[0];
									if (!cache[s]) {
										cache[s] = Ext.select(s)
									}
									cache[s].on(parts[1], o[b])
								}
							}
							cache = null
						},
						id : function(el, prefix) {
							prefix = prefix || "ext-gen";
							el = Ext.getDom(el);
							var id = prefix + (++idSeed);
							return el ? (el.id ? el.id : (el.id = id)) : id
						},
						extend : function() {
							var io = function(o) {
								for ( var m in o) {
									this[m] = o[m]
								}
							};
							var oc = Object.prototype.constructor;
							return function(sb, sp, overrides) {
								if (typeof sp == "object") {
									overrides = sp;
									sp = sb;
									sb = overrides.constructor != oc ? overrides.constructor
											: function() {
												sp.apply(this, arguments)
											}
								}
								var F = function() {
								}, sbp, spp = sp.prototype;
								F.prototype = spp;
								sbp = sb.prototype = new F();
								sbp.constructor = sb;
								sb.superclass = spp;
								if (spp.constructor == oc) {
									spp.constructor = sp
								}
								sb.override = function(o) {
									Ext.override(sb, o)
								};
								sbp.override = io;
								Ext.override(sb, overrides);
								sb.extend = function(o) {
									Ext.extend(sb, o)
								};
								return sb
							}
						}(),
						override : function(origclass, overrides) {
							if (overrides) {
								var p = origclass.prototype;
								for ( var method in overrides) {
									p[method] = overrides[method]
								}
							}
						},
						namespace : function() {
							var a = arguments, o = null, i, j, d, rt;
							for (i = 0; i < a.length; ++i) {
								d = a[i].split(".");
								rt = d[0];
								eval("if (typeof " + rt + " == \"undefined\"){"
										+ rt + " = {};} o = " + rt + ";");
								for (j = 1; j < d.length; ++j) {
									o[d[j]] = o[d[j]] || {};
									o = o[d[j]]
								}
							}
						},
						urlEncode : function(o) {
							if (!o) {
								return ""
							}
							var buf = [];
							for ( var key in o) {
								var ov = o[key], k = encodeURIComponent(key);
								var type = typeof ov;
								if (type == "undefined") {
									buf.push(k, "=&")
								} else {
									if (type != "function" && type != "object") {
										buf.push(k, "=",
												encodeURIComponent(ov), "&")
									} else {
										if (Ext.isArray(ov)) {
											if (ov.length) {
												for ( var i = 0, len = ov.length; i < len; i++) {
													buf
															.push(
																	k,
																	"=",
																	encodeURIComponent(ov[i] === undefined ? ""
																			: ov[i]),
																	"&")
												}
											} else {
												buf.push(k, "=&")
											}
										}
									}
								}
							}
							buf.pop();
							return buf.join("")
						},
						urlDecode : function(string, overwrite) {
							if (!string || !string.length) {
								return {}
							}
							var obj = {};
							var pairs = string.split("&");
							var pair, name, value;
							for ( var i = 0, len = pairs.length; i < len; i++) {
								pair = pairs[i].split("=");
								name = decodeURIComponent(pair[0]);
								value = decodeURIComponent(pair[1]);
								if (overwrite !== true) {
									if (typeof obj[name] == "undefined") {
										obj[name] = value
									} else {
										if (typeof obj[name] == "string") {
											obj[name] = [ obj[name] ];
											obj[name].push(value)
										} else {
											obj[name].push(value)
										}
									}
								} else {
									obj[name] = value
								}
							}
							return obj
						},
						each : function(array, fn, scope) {
							if (typeof array.length == "undefined"
									|| typeof array == "string") {
								array = [ array ]
							}
							for ( var i = 0, len = array.length; i < len; i++) {
								if (fn.call(scope || array[i], array[i], i,
										array) === false) {
									return i
								}
							}
						},
						combine : function() {
							var as = arguments, l = as.length, r = [];
							for ( var i = 0; i < l; i++) {
								var a = as[i];
								if (Ext.isArray(a)) {
									r = r.concat(a)
								} else {
									if (a.length !== undefined && !a.substr) {
										r = r.concat(Array.prototype.slice
												.call(a, 0))
									} else {
										r.push(a)
									}
								}
							}
							return r
						},
						escapeRe : function(s) {
							return s.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1")
						},
						callback : function(cb, scope, args, delay) {
							if (typeof cb == "function") {
								if (delay) {
									cb.defer(delay, scope, args || [])
								} else {
									cb.apply(scope, args || [])
								}
							}
						},
						getDom : function(el) {
							if (!el || !document) {
								return null
							}
							return el.dom ? el.dom
									: (typeof el == "string" ? document
											.getElementById(el) : el)
						},
						getDoc : function() {
							return Ext.get(document)
						},
						getBody : function() {
							return Ext.get(document.body
									|| document.documentElement)
						},
						getCmp : function(id) {
							return Ext.ComponentMgr.get(id)
						},
						num : function(v, defaultValue) {
							if (typeof v != "number") {
								return defaultValue
							}
							return v
						},
						destroy : function() {
							for ( var i = 0, a = arguments, len = a.length; i < len; i++) {
								var as = a[i];
								if (as) {
									if (typeof as.destroy == "function") {
										as.destroy()
									} else {
										if (as.dom) {
											as.removeAllListeners();
											as.remove()
										}
									}
								}
							}
						},
						removeNode : isIE ? function() {
							var d;
							return function(n) {
								if (n && n.tagName != "BODY") {
									d = d || document.createElement("div");
									d.appendChild(n);
									d.innerHTML = ""
								}
							}
						}() : function(n) {
							if (n && n.parentNode && n.tagName != "BODY") {
								n.parentNode.removeChild(n)
							}
						},
						type : function(o) {
							if (o === undefined || o === null) {
								return false
							}
							if (o.htmlElement) {
								return "element"
							}
							var t = typeof o;
							if (t == "object" && o.nodeName) {
								switch (o.nodeType) {
								case 1:
									return "element";
								case 3:
									return (/\S/).test(o.nodeValue) ? "textnode"
											: "whitespace"
								}
							}
							if (t == "object" || t == "function") {
								switch (o.constructor) {
								case Array:
									return "array";
								case RegExp:
									return "regexp"
								}
								if (typeof o.length == "number"
										&& typeof o.item == "function") {
									return "nodelist"
								}
							}
							return t
						},
						isEmpty : function(v, allowBlank) {
							return v === null || v === undefined
									|| (!allowBlank ? v === "" : false)
						},
						value : function(v, defaultValue, allowBlank) {
							return Ext.isEmpty(v, allowBlank) ? defaultValue
									: v
						},
						isArray : function(v) {
							return v && typeof v.pop == "function"
						},
						isDate : function(v) {
							return v && typeof v.getFullYear == "function"
						},
						isOpera : isOpera,
						isSafari : isSafari,
						isSafari3 : isSafari3,
						isSafari2 : isSafari && !isSafari3,
						isIE : isIE,
						isIE6 : isIE && !isIE7,
						isIE7 : isIE7,
						isGecko : isGecko,
						isBorderBox : isBorderBox,
						isLinux : isLinux,
						isWindows : isWindows,
						isMac : isMac,
						isAir : isAir,
						useShims : ((isIE && !isIE7) || (isGecko && isMac))
					});
	Ext.ns = Ext.namespace
})();
Ext.ns("Ext", "Ext.util", "Ext.grid", "Ext.dd", "Ext.tree", "Ext.data",
		"Ext.form", "Ext.menu", "Ext.state", "Ext.lib", "Ext.layout",
		"Ext.app", "Ext.ux");
Ext.apply(Function.prototype, {
	createCallback : function() {
		var A = arguments;
		var B = this;
		return function() {
			return B.apply(window, A)
		}
	},
	createDelegate : function(C, B, A) {
		var D = this;
		return function() {
			var F = B || arguments;
			if (A === true) {
				F = Array.prototype.slice.call(arguments, 0);
				F = F.concat(B)
			} else {
				if (typeof A == "number") {
					F = Array.prototype.slice.call(arguments, 0);
					var E = [ A, 0 ].concat(B);
					Array.prototype.splice.apply(F, E)
				}
			}
			return D.apply(C || window, F)
		}
	},
	defer : function(C, E, B, A) {
		var D = this.createDelegate(E, B, A);
		if (C) {
			return setTimeout(D, C)
		}
		D();
		return 0
	},
	createSequence : function(B, A) {
		if (typeof B != "function") {
			return this
		}
		var C = this;
		return function() {
			var D = C.apply(this || window, arguments);
			B.apply(A || this || window, arguments);
			return D
		}
	},
	createInterceptor : function(B, A) {
		if (typeof B != "function") {
			return this
		}
		var C = this;
		return function() {
			B.target = this;
			B.method = C;
			if (B.apply(A || this || window, arguments) === false) {
				return
			}
			return C.apply(this || window, arguments)
		}
	}
});
Ext.applyIf(String, {
	escape : function(A) {
		return A.replace(/('|\\)/g, "\\$1")
	},
	leftPad : function(D, B, C) {
		var A = new String(D);
		if (!C) {
			C = " "
		}
		while (A.length < B) {
			A = C + A
		}
		return A.toString()
	},
	format : function(B) {
		var A = Array.prototype.slice.call(arguments, 1);
		return B.replace(/\{(\d+)\}/g, function(C, D) {
			return A[D]
		})
	}
});
String.prototype.toggle = function(B, A) {
	return this == B ? A : B
};
String.prototype.trim = function() {
	var A = /^\s+|\s+$/g;
	return function() {
		return this.replace(A, "")
	}
}();
Ext.applyIf(Number.prototype, {
	constrain : function(B, A) {
		return Math.min(Math.max(this, B), A)
	}
});
Ext.applyIf(Array.prototype, {
	indexOf : function(C) {
		for ( var B = 0, A = this.length; B < A; B++) {
			if (this[B] == C) {
				return B
			}
		}
		return -1
	},
	remove : function(B) {
		var A = this.indexOf(B);
		if (A != -1) {
			this.splice(A, 1)
		}
		return this
	}
});
Date.prototype.getElapsed = function(A) {
	return Math.abs((A || new Date()).getTime() - this.getTime())
};
(function() {
	var B;
	Ext.lib.Dom = {
		getViewWidth : function(E) {
			return E ? this.getDocumentWidth() : this.getViewportWidth()
		},
		getViewHeight : function(E) {
			return E ? this.getDocumentHeight() : this.getViewportHeight()
		},
		getDocumentHeight : function() {
			var E = (document.compatMode != "CSS1Compat") ? document.body.scrollHeight
					: document.documentElement.scrollHeight;
			return Math.max(E, this.getViewportHeight())
		},
		getDocumentWidth : function() {
			var E = (document.compatMode != "CSS1Compat") ? document.body.scrollWidth
					: document.documentElement.scrollWidth;
			return Math.max(E, this.getViewportWidth())
		},
		getViewportHeight : function() {
			if (Ext.isIE) {
				return Ext.isStrict ? document.documentElement.clientHeight
						: document.body.clientHeight
			} else {
				return self.innerHeight
			}
		},
		getViewportWidth : function() {
			if (Ext.isIE) {
				return Ext.isStrict ? document.documentElement.clientWidth
						: document.body.clientWidth
			} else {
				return self.innerWidth
			}
		},
		isAncestor : function(F, G) {
			F = Ext.getDom(F);
			G = Ext.getDom(G);
			if (!F || !G) {
				return false
			}
			if (F.contains && !Ext.isSafari) {
				return F.contains(G)
			} else {
				if (F.compareDocumentPosition) {
					return !!(F.compareDocumentPosition(G) & 16)
				} else {
					var E = G.parentNode;
					while (E) {
						if (E == F) {
							return true
						} else {
							if (!E.tagName || E.tagName.toUpperCase() == "HTML") {
								return false
							}
						}
						E = E.parentNode
					}
					return false
				}
			}
		},
		getRegion : function(E) {
			return Ext.lib.Region.getRegion(E)
		},
		getY : function(E) {
			return this.getXY(E)[1]
		},
		getX : function(E) {
			return this.getXY(E)[0]
		},
		getXY : function(G) {
			var F, K, M, N, J = (document.body || document.documentElement);
			G = Ext.getDom(G);
			if (G == J) {
				return [ 0, 0 ]
			}
			if (G.getBoundingClientRect) {
				M = G.getBoundingClientRect();
				N = C(document).getScroll();
				return [ M.left + N.left, M.top + N.top ]
			}
			var O = 0, L = 0;
			F = G;
			var E = C(G).getStyle("position") == "absolute";
			while (F) {
				O += F.offsetLeft;
				L += F.offsetTop;
				if (!E && C(F).getStyle("position") == "absolute") {
					E = true
				}
				if (Ext.isGecko) {
					K = C(F);
					var P = parseInt(K.getStyle("borderTopWidth"), 10) || 0;
					var H = parseInt(K.getStyle("borderLeftWidth"), 10) || 0;
					O += H;
					L += P;
					if (F != G && K.getStyle("overflow") != "visible") {
						O += H;
						L += P
					}
				}
				F = F.offsetParent
			}
			if (Ext.isSafari && E) {
				O -= J.offsetLeft;
				L -= J.offsetTop
			}
			if (Ext.isGecko && !E) {
				var I = C(J);
				O += parseInt(I.getStyle("borderLeftWidth"), 10) || 0;
				L += parseInt(I.getStyle("borderTopWidth"), 10) || 0
			}
			F = G.parentNode;
			while (F && F != J) {
				if (!Ext.isOpera
						|| (F.tagName != "TR" && C(F).getStyle("display") != "inline")) {
					O -= F.scrollLeft;
					L -= F.scrollTop
				}
				F = F.parentNode
			}
			return [ O, L ]
		},
		setXY : function(E, F) {
			E = Ext.fly(E, "_setXY");
			E.position();
			var G = E.translatePoints(F);
			if (F[0] !== false) {
				E.dom.style.left = G.left + "px"
			}
			if (F[1] !== false) {
				E.dom.style.top = G.top + "px"
			}
		},
		setX : function(F, E) {
			this.setXY(F, [ E, false ])
		},
		setY : function(E, F) {
			this.setXY(E, [ false, F ])
		}
	};
	Ext.lib.Event = function() {
		var F = false;
		var G = [];
		var K = [];
		var I = 0;
		var H = [];
		var E = 0;
		var J = null;
		return {
			POLL_RETRYS : 200,
			POLL_INTERVAL : 20,
			EL : 0,
			TYPE : 1,
			FN : 2,
			WFN : 3,
			OBJ : 3,
			ADJ_SCOPE : 4,
			_interval : null,
			startInterval : function() {
				if (!this._interval) {
					var L = this;
					var M = function() {
						L._tryPreloadAttach()
					};
					this._interval = setInterval(M, this.POLL_INTERVAL)
				}
			},
			onAvailable : function(N, L, O, M) {
				H.push( {
					id : N,
					fn : L,
					obj : O,
					override : M,
					checkReady : false
				});
				I = this.POLL_RETRYS;
				this.startInterval()
			},
			addListener : function(Q, M, P) {
				Q = Ext.getDom(Q);
				if (!Q || !P) {
					return false
				}
				if ("unload" == M) {
					K[K.length] = [ Q, M, P ];
					return true
				}
				var O = function(R) {
					return typeof Ext != "undefined" ? P(Ext.lib.Event
							.getEvent(R)) : false
				};
				var L = [ Q, M, P, O ];
				var N = G.length;
				G[N] = L;
				this.doAdd(Q, M, O, false);
				return true
			},
			removeListener : function(S, O, R) {
				var Q, N;
				S = Ext.getDom(S);
				if (!R) {
					return this.purgeElement(S, false, O)
				}
				if ("unload" == O) {
					for (Q = 0, N = K.length; Q < N; Q++) {
						var M = K[Q];
						if (M && M[0] == S && M[1] == O && M[2] == R) {
							K.splice(Q, 1);
							return true
						}
					}
					return false
				}
				var L = null;
				var P = arguments[3];
				if ("undefined" == typeof P) {
					P = this._getCacheIndex(S, O, R)
				}
				if (P >= 0) {
					L = G[P]
				}
				if (!S || !L) {
					return false
				}
				this.doRemove(S, O, L[this.WFN], false);
				delete G[P][this.WFN];
				delete G[P][this.FN];
				G.splice(P, 1);
				return true
			},
			getTarget : function(N, M) {
				N = N.browserEvent || N;
				var L = N.target || N.srcElement;
				return this.resolveTextNode(L)
			},
			resolveTextNode : function(L) {
				if (Ext.isSafari && L && 3 == L.nodeType) {
					return L.parentNode
				} else {
					return L
				}
			},
			getPageX : function(M) {
				M = M.browserEvent || M;
				var L = M.pageX;
				if (!L && 0 !== L) {
					L = M.clientX || 0;
					if (Ext.isIE) {
						L += this.getScroll()[1]
					}
				}
				return L
			},
			getPageY : function(L) {
				L = L.browserEvent || L;
				var M = L.pageY;
				if (!M && 0 !== M) {
					M = L.clientY || 0;
					if (Ext.isIE) {
						M += this.getScroll()[0]
					}
				}
				return M
			},
			getXY : function(L) {
				L = L.browserEvent || L;
				return [ this.getPageX(L), this.getPageY(L) ]
			},
			getRelatedTarget : function(M) {
				M = M.browserEvent || M;
				var L = M.relatedTarget;
				if (!L) {
					if (M.type == "mouseout") {
						L = M.toElement
					} else {
						if (M.type == "mouseover") {
							L = M.fromElement
						}
					}
				}
				return this.resolveTextNode(L)
			},
			getTime : function(N) {
				N = N.browserEvent || N;
				if (!N.time) {
					var M = new Date().getTime();
					try {
						N.time = M
					} catch (L) {
						this.lastError = L;
						return M
					}
				}
				return N.time
			},
			stopEvent : function(L) {
				this.stopPropagation(L);
				this.preventDefault(L)
			},
			stopPropagation : function(L) {
				L = L.browserEvent || L;
				if (L.stopPropagation) {
					L.stopPropagation()
				} else {
					L.cancelBubble = true
				}
			},
			preventDefault : function(L) {
				L = L.browserEvent || L;
				if (L.preventDefault) {
					L.preventDefault()
				} else {
					L.returnValue = false
				}
			},
			getEvent : function(M) {
				var L = M || window.event;
				if (!L) {
					var N = this.getEvent.caller;
					while (N) {
						L = N.arguments[0];
						if (L && Event == L.constructor) {
							break
						}
						N = N.caller
					}
				}
				return L
			},
			getCharCode : function(L) {
				L = L.browserEvent || L;
				return L.charCode || L.keyCode || 0
			},
			_getCacheIndex : function(Q, N, P) {
				for ( var O = 0, M = G.length; O < M; ++O) {
					var L = G[O];
					if (L && L[this.FN] == P && L[this.EL] == Q
							&& L[this.TYPE] == N) {
						return O
					}
				}
				return -1
			},
			elCache : {},
			getEl : function(L) {
				return document.getElementById(L)
			},
			clearCache : function() {
			},
			_load : function(M) {
				F = true;
				var L = Ext.lib.Event;
				if (Ext.isIE) {
					L.doRemove(window, "load", L._load)
				}
			},
			_tryPreloadAttach : function() {
				if (this.locked) {
					return false
				}
				this.locked = true;
				var R = !F;
				if (!R) {
					R = (I > 0)
				}
				var Q = [];
				for ( var M = 0, L = H.length; M < L; ++M) {
					var P = H[M];
					if (P) {
						var O = this.getEl(P.id);
						if (O) {
							if (!P.checkReady || F || O.nextSibling
									|| (document && document.body)) {
								var N = O;
								if (P.override) {
									if (P.override === true) {
										N = P.obj
									} else {
										N = P.override
									}
								}
								P.fn.call(N, P.obj);
								H[M] = null
							}
						} else {
							Q.push(P)
						}
					}
				}
				I = (Q.length === 0) ? 0 : I - 1;
				if (R) {
					this.startInterval()
				} else {
					clearInterval(this._interval);
					this._interval = null
				}
				this.locked = false;
				return true
			},
			purgeElement : function(P, Q, N) {
				var R = this.getListeners(P, N);
				if (R) {
					for ( var O = 0, L = R.length; O < L; ++O) {
						var M = R[O];
						this.removeListener(P, M.type, M.fn)
					}
				}
				if (Q && P && P.childNodes) {
					for (O = 0, L = P.childNodes.length; O < L; ++O) {
						this.purgeElement(P.childNodes[O], Q, N)
					}
				}
			},
			getListeners : function(M, R) {
				var P = [], L;
				if (!R) {
					L = [ G, K ]
				} else {
					if (R == "unload") {
						L = [ K ]
					} else {
						L = [ G ]
					}
				}
				for ( var O = 0; O < L.length; ++O) {
					var T = L[O];
					if (T && T.length > 0) {
						for ( var Q = 0, S = T.length; Q < S; ++Q) {
							var N = T[Q];
							if (N && N[this.EL] === M
									&& (!R || R === N[this.TYPE])) {
								P.push( {
									type : N[this.TYPE],
									fn : N[this.FN],
									obj : N[this.OBJ],
									adjust : N[this.ADJ_SCOPE],
									index : Q
								})
							}
						}
					}
				}
				return (P.length) ? P : null
			},
			_unload : function(S) {
				var R = Ext.lib.Event, P, O, M, L, N;
				for (P = 0, L = K.length; P < L; ++P) {
					M = K[P];
					if (M) {
						var Q = window;
						if (M[R.ADJ_SCOPE]) {
							if (M[R.ADJ_SCOPE] === true) {
								Q = M[R.OBJ]
							} else {
								Q = M[R.ADJ_SCOPE]
							}
						}
						M[R.FN].call(Q, R.getEvent(S), M[R.OBJ]);
						K[P] = null;
						M = null;
						Q = null
					}
				}
				K = null;
				if (G && G.length > 0) {
					O = G.length;
					while (O) {
						N = O - 1;
						M = G[N];
						if (M) {
							R.removeListener(M[R.EL], M[R.TYPE], M[R.FN], N)
						}
						O = O - 1
					}
					M = null;
					R.clearCache()
				}
				R.doRemove(window, "unload", R._unload)
			},
			getScroll : function() {
				var L = document.documentElement, M = document.body;
				if (L && (L.scrollTop || L.scrollLeft)) {
					return [ L.scrollTop, L.scrollLeft ]
				} else {
					if (M) {
						return [ M.scrollTop, M.scrollLeft ]
					} else {
						return [ 0, 0 ]
					}
				}
			},
			doAdd : function() {
				if (window.addEventListener) {
					return function(O, M, N, L) {
						O.addEventListener(M, N, (L))
					}
				} else {
					if (window.attachEvent) {
						return function(O, M, N, L) {
							O.attachEvent("on" + M, N)
						}
					} else {
						return function() {
						}
					}
				}
			}(),
			doRemove : function() {
				if (window.removeEventListener) {
					return function(O, M, N, L) {
						O.removeEventListener(M, N, (L))
					}
				} else {
					if (window.detachEvent) {
						return function(N, L, M) {
							N.detachEvent("on" + L, M)
						}
					} else {
						return function() {
						}
					}
				}
			}()
		}
	}();
	var D = Ext.lib.Event;
	D.on = D.addListener;
	D.un = D.removeListener;
	if (document && document.body) {
		D._load()
	} else {
		D.doAdd(window, "load", D._load)
	}
	D.doAdd(window, "unload", D._unload);
	D._tryPreloadAttach();
	Ext.lib.Ajax = {
		request : function(K, I, E, J, F) {
			if (F) {
				var G = F.headers;
				if (G) {
					for ( var H in G) {
						if (G.hasOwnProperty(H)) {
							this.initHeader(H, G[H], false)
						}
					}
				}
				if (F.xmlData) {
					this.initHeader("Content-Type", "text/xml", false);
					K = "POST";
					J = F.xmlData
				} else {
					if (F.jsonData) {
						this.initHeader("Content-Type", "text/javascript",
								false);
						K = "POST";
						J = typeof F.jsonData == "object" ? Ext
								.encode(F.jsonData) : F.jsonData
					}
				}
			}
			return this.asyncRequest(K, I, E, J)
		},
		serializeForm : function(F) {
			if (typeof F == "string") {
				F = (document.getElementById(F) || document.forms[F])
			}
			var G, E, H, J, K = "", M = false;
			for ( var L = 0; L < F.elements.length; L++) {
				G = F.elements[L];
				J = F.elements[L].disabled;
				E = F.elements[L].name;
				H = F.elements[L].value;
				if (!J && E) {
					switch (G.type) {
					case "select-one":
					case "select-multiple":
						for ( var I = 0; I < G.options.length; I++) {
							if (G.options[I].selected) {
								if (Ext.isIE) {
									K += encodeURIComponent(E)
											+ "="
											+ encodeURIComponent(G.options[I].attributes["value"].specified ? G.options[I].value
													: G.options[I].text) + "&"
								} else {
									K += encodeURIComponent(E)
											+ "="
											+ encodeURIComponent(G.options[I]
													.hasAttribute("value") ? G.options[I].value
													: G.options[I].text) + "&"
								}
							}
						}
						break;
					case "radio":
					case "checkbox":
						if (G.checked) {
							K += encodeURIComponent(E) + "="
									+ encodeURIComponent(H) + "&"
						}
						break;
					case "file":
					case undefined:
					case "reset":
					case "button":
						break;
					case "submit":
						if (M == false) {
							K += encodeURIComponent(E) + "="
									+ encodeURIComponent(H) + "&";
							M = true
						}
						break;
					default:
						K += encodeURIComponent(E) + "="
								+ encodeURIComponent(H) + "&";
						break
					}
				}
			}
			K = K.substr(0, K.length - 1);
			return K
		},
		headers : {},
		hasHeaders : false,
		useDefaultHeader : true,
		defaultPostHeader : "application/x-www-form-urlencoded",
		useDefaultXhrHeader : true,
		defaultXhrHeader : "XMLHttpRequest",
		hasDefaultHeaders : true,
		defaultHeaders : {},
		poll : {},
		timeout : {},
		pollInterval : 50,
		transactionId : 0,
		setProgId : function(E) {
			this.activeX.unshift(E)
		},
		setDefaultPostHeader : function(E) {
			this.useDefaultHeader = E
		},
		setDefaultXhrHeader : function(E) {
			this.useDefaultXhrHeader = E
		},
		setPollingInterval : function(E) {
			if (typeof E == "number" && isFinite(E)) {
				this.pollInterval = E
			}
		},
		createXhrObject : function(I) {
			var H, E;
			try {
				E = new XMLHttpRequest();
				H = {
					conn : E,
					tId : I
				}
			} catch (G) {
				for ( var F = 0; F < this.activeX.length; ++F) {
					try {
						E = new ActiveXObject(this.activeX[F]);
						H = {
							conn : E,
							tId : I
						};
						break
					} catch (G) {
					}
				}
			} finally {
				return H
			}
		},
		getConnectionObject : function() {
			var F;
			var G = this.transactionId;
			try {
				F = this.createXhrObject(G);
				if (F) {
					this.transactionId++
				}
			} catch (E) {
			} finally {
				return F
			}
		},
		asyncRequest : function(I, F, H, E) {
			var G = this.getConnectionObject();
			if (!G) {
				return null
			} else {
				G.conn.open(I, F, true);
				if (this.useDefaultXhrHeader) {
					if (!this.defaultHeaders["X-Requested-With"]) {
						this.initHeader("X-Requested-With",
								this.defaultXhrHeader, true)
					}
				}
				if (E && this.useDefaultHeader) {
					this.initHeader("Content-Type", this.defaultPostHeader)
				}
				if (this.hasDefaultHeaders || this.hasHeaders) {
					this.setHeader(G)
				}
				this.handleReadyState(G, H);
				G.conn.send(E || null);
				return G
			}
		},
		handleReadyState : function(F, G) {
			var E = this;
			if (G && G.timeout) {
				this.timeout[F.tId] = window.setTimeout(function() {
					E.abort(F, G, true)
				}, G.timeout)
			}
			this.poll[F.tId] = window.setInterval(function() {
				if (F.conn && F.conn.readyState == 4) {
					window.clearInterval(E.poll[F.tId]);
					delete E.poll[F.tId];
					if (G && G.timeout) {
						window.clearTimeout(E.timeout[F.tId]);
						delete E.timeout[F.tId]
					}
					E.handleTransactionResponse(F, G)
				}
			}, this.pollInterval)
		},
		handleTransactionResponse : function(I, J, E) {
			if (!J) {
				this.releaseObject(I);
				return
			}
			var G, F;
			try {
				if (I.conn.status !== undefined && I.conn.status != 0) {
					G = I.conn.status
				} else {
					G = 13030
				}
			} catch (H) {
				G = 13030
			}
			if (G >= 200 && G < 300) {
				F = this.createResponseObject(I, J.argument);
				if (J.success) {
					if (!J.scope) {
						J.success(F)
					} else {
						J.success.apply(J.scope, [ F ])
					}
				}
			} else {
				switch (G) {
				case 12002:
				case 12029:
				case 12030:
				case 12031:
				case 12152:
				case 13030:
					F = this.createExceptionObject(I.tId, J.argument, (E ? E
							: false));
					if (J.failure) {
						if (!J.scope) {
							J.failure(F)
						} else {
							J.failure.apply(J.scope, [ F ])
						}
					}
					break;
				default:
					F = this.createResponseObject(I, J.argument);
					if (J.failure) {
						if (!J.scope) {
							J.failure(F)
						} else {
							J.failure.apply(J.scope, [ F ])
						}
					}
				}
			}
			this.releaseObject(I);
			F = null
		},
		createResponseObject : function(E, K) {
			var H = {};
			var M = {};
			try {
				var G = E.conn.getAllResponseHeaders();
				var J = G.split("\n");
				for ( var I = 0; I < J.length; I++) {
					var F = J[I].indexOf(":");
					if (F != -1) {
						M[J[I].substring(0, F)] = J[I].substring(F + 2)
					}
				}
			} catch (L) {
			}
			H.tId = E.tId;
			H.status = E.conn.status;
			H.statusText = E.conn.statusText;
			H.getResponseHeader = M;
			H.getAllResponseHeaders = G;
			H.responseText = E.conn.responseText;
			H.responseXML = E.conn.responseXML;
			if (typeof K !== undefined) {
				H.argument = K
			}
			return H
		},
		createExceptionObject : function(L, H, E) {
			var J = 0;
			var K = "communication failure";
			var G = -1;
			var F = "transaction aborted";
			var I = {};
			I.tId = L;
			if (E) {
				I.status = G;
				I.statusText = F
			} else {
				I.status = J;
				I.statusText = K
			}
			if (H) {
				I.argument = H
			}
			return I
		},
		initHeader : function(E, H, G) {
			var F = (G) ? this.defaultHeaders : this.headers;
			if (F[E] === undefined) {
				F[E] = H
			} else {
				F[E] = H + "," + F[E]
			}
			if (G) {
				this.hasDefaultHeaders = true
			} else {
				this.hasHeaders = true
			}
		},
		setHeader : function(E) {
			if (this.hasDefaultHeaders) {
				for ( var F in this.defaultHeaders) {
					if (this.defaultHeaders.hasOwnProperty(F)) {
						E.conn.setRequestHeader(F, this.defaultHeaders[F])
					}
				}
			}
			if (this.hasHeaders) {
				for ( var F in this.headers) {
					if (this.headers.hasOwnProperty(F)) {
						E.conn.setRequestHeader(F, this.headers[F])
					}
				}
				this.headers = {};
				this.hasHeaders = false
			}
		},
		resetDefaultHeaders : function() {
			delete this.defaultHeaders;
			this.defaultHeaders = {};
			this.hasDefaultHeaders = false
		},
		abort : function(F, G, E) {
			if (this.isCallInProgress(F)) {
				F.conn.abort();
				window.clearInterval(this.poll[F.tId]);
				delete this.poll[F.tId];
				if (E) {
					delete this.timeout[F.tId]
				}
				this.handleTransactionResponse(F, G, true);
				return true
			} else {
				return false
			}
		},
		isCallInProgress : function(E) {
			if (E.conn) {
				return E.conn.readyState != 4 && E.conn.readyState != 0
			} else {
				return false
			}
		},
		releaseObject : function(E) {
			E.conn = null;
			E = null
		},
		activeX : [ "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP" ]
	};
	Ext.lib.Region = function(G, H, E, F) {
		this.top = G;
		this[1] = G;
		this.right = H;
		this.bottom = E;
		this.left = F;
		this[0] = F
	};
	Ext.lib.Region.prototype = {
		contains : function(E) {
			return (E.left >= this.left && E.right <= this.right
					&& E.top >= this.top && E.bottom <= this.bottom)
		},
		getArea : function() {
			return ((this.bottom - this.top) * (this.right - this.left))
		},
		intersect : function(I) {
			var G = Math.max(this.top, I.top);
			var H = Math.min(this.right, I.right);
			var E = Math.min(this.bottom, I.bottom);
			var F = Math.max(this.left, I.left);
			if (E >= G && H >= F) {
				return new Ext.lib.Region(G, H, E, F)
			} else {
				return null
			}
		},
		union : function(I) {
			var G = Math.min(this.top, I.top);
			var H = Math.max(this.right, I.right);
			var E = Math.max(this.bottom, I.bottom);
			var F = Math.min(this.left, I.left);
			return new Ext.lib.Region(G, H, E, F)
		},
		constrainTo : function(E) {
			this.top = this.top.constrain(E.top, E.bottom);
			this.bottom = this.bottom.constrain(E.top, E.bottom);
			this.left = this.left.constrain(E.left, E.right);
			this.right = this.right.constrain(E.left, E.right);
			return this
		},
		adjust : function(G, F, E, H) {
			this.top += G;
			this.left += F;
			this.right += H;
			this.bottom += E;
			return this
		}
	};
	Ext.lib.Region.getRegion = function(H) {
		var J = Ext.lib.Dom.getXY(H);
		var G = J[1];
		var I = J[0] + H.offsetWidth;
		var E = J[1] + H.offsetHeight;
		var F = J[0];
		return new Ext.lib.Region(G, I, E, F)
	};
	Ext.lib.Point = function(E, F) {
		if (Ext.isArray(E)) {
			F = E[1];
			E = E[0]
		}
		this.x = this.right = this.left = this[0] = E;
		this.y = this.top = this.bottom = this[1] = F
	};
	Ext.lib.Point.prototype = new Ext.lib.Region();
	Ext.lib.Anim = {
		scroll : function(H, F, I, J, E, G) {
			return this.run(H, F, I, J, E, G, Ext.lib.Scroll)
		},
		motion : function(H, F, I, J, E, G) {
			return this.run(H, F, I, J, E, G, Ext.lib.Motion)
		},
		color : function(H, F, I, J, E, G) {
			return this.run(H, F, I, J, E, G, Ext.lib.ColorAnim)
		},
		run : function(I, F, K, L, E, H, G) {
			G = G || Ext.lib.AnimBase;
			if (typeof L == "string") {
				L = Ext.lib.Easing[L]
			}
			var J = new G(I, F, K, L);
			J.animateX(function() {
				Ext.callback(E, H)
			});
			return J
		}
	};
	function C(E) {
		if (!B) {
			B = new Ext.Element.Flyweight()
		}
		B.dom = E;
		return B
	}
	if (Ext.isIE) {
		function A() {
			var E = Function.prototype;
			delete E.createSequence;
			delete E.defer;
			delete E.createDelegate;
			delete E.createCallback;
			delete E.createInterceptor;
			window.detachEvent("onunload", A)
		}
		window.attachEvent("onunload", A)
	}
	Ext.lib.AnimBase = function(F, E, G, H) {
		if (F) {
			this.init(F, E, G, H)
		}
	};
	Ext.lib.AnimBase.prototype = {
		toString : function() {
			var E = this.getEl();
			var F = E.id || E.tagName;
			return ("Anim " + F)
		},
		patterns : {
			noNegatives : /width|height|opacity|padding/i,
			offsetAttribute : /^((width|height)|(top|left))$/,
			defaultUnit : /width|height|top$|bottom$|left$|right$/i,
			offsetUnit : /\d+(em|%|en|ex|pt|in|cm|mm|pc)$/i
		},
		doMethod : function(E, G, F) {
			return this.method(this.currentFrame, G, F - G, this.totalFrames)
		},
		setAttribute : function(E, G, F) {
			if (this.patterns.noNegatives.test(E)) {
				G = (G > 0) ? G : 0
			}
			Ext.fly(this.getEl(), "_anim").setStyle(E, G + F)
		},
		getAttribute : function(E) {
			var G = this.getEl();
			var I = C(G).getStyle(E);
			if (I !== "auto" && !this.patterns.offsetUnit.test(I)) {
				return parseFloat(I)
			}
			var F = this.patterns.offsetAttribute.exec(E) || [];
			var J = !!(F[3]);
			var H = !!(F[2]);
			if (H || (C(G).getStyle("position") == "absolute" && J)) {
				I = G["offset" + F[0].charAt(0).toUpperCase() + F[0].substr(1)]
			} else {
				I = 0
			}
			return I
		},
		getDefaultUnit : function(E) {
			if (this.patterns.defaultUnit.test(E)) {
				return "px"
			}
			return ""
		},
		animateX : function(G, E) {
			var F = function() {
				this.onComplete.removeListener(F);
				if (typeof G == "function") {
					G.call(E || this, this)
				}
			};
			this.onComplete.addListener(F, this);
			this.animate()
		},
		setRuntimeAttribute : function(F) {
			var K;
			var G;
			var H = this.attributes;
			this.runtimeAttributes[F] = {};
			var J = function(L) {
				return (typeof L !== "undefined")
			};
			if (!J(H[F]["to"]) && !J(H[F]["by"])) {
				return false
			}
			K = (J(H[F]["from"])) ? H[F]["from"] : this.getAttribute(F);
			if (J(H[F]["to"])) {
				G = H[F]["to"]
			} else {
				if (J(H[F]["by"])) {
					if (K.constructor == Array) {
						G = [];
						for ( var I = 0, E = K.length; I < E; ++I) {
							G[I] = K[I] + H[F]["by"][I]
						}
					} else {
						G = K + H[F]["by"]
					}
				}
			}
			this.runtimeAttributes[F].start = K;
			this.runtimeAttributes[F].end = G;
			this.runtimeAttributes[F].unit = (J(H[F].unit)) ? H[F]["unit"]
					: this.getDefaultUnit(F)
		},
		init : function(G, L, K, E) {
			var F = false;
			var H = null;
			var J = 0;
			G = Ext.getDom(G);
			this.attributes = L || {};
			this.duration = K || 1;
			this.method = E || Ext.lib.Easing.easeNone;
			this.useSeconds = true;
			this.currentFrame = 0;
			this.totalFrames = Ext.lib.AnimMgr.fps;
			this.getEl = function() {
				return G
			};
			this.isAnimated = function() {
				return F
			};
			this.getStartTime = function() {
				return H
			};
			this.runtimeAttributes = {};
			this.animate = function() {
				if (this.isAnimated()) {
					return false
				}
				this.currentFrame = 0;
				this.totalFrames = (this.useSeconds) ? Math
						.ceil(Ext.lib.AnimMgr.fps * this.duration)
						: this.duration;
				Ext.lib.AnimMgr.registerElement(this)
			};
			this.stop = function(O) {
				if (O) {
					this.currentFrame = this.totalFrames;
					this._onTween.fire()
				}
				Ext.lib.AnimMgr.stop(this)
			};
			var N = function() {
				this.onStart.fire();
				this.runtimeAttributes = {};
				for ( var O in this.attributes) {
					this.setRuntimeAttribute(O)
				}
				F = true;
				J = 0;
				H = new Date()
			};
			var M = function() {
				var Q = {
					duration : new Date() - this.getStartTime(),
					currentFrame : this.currentFrame
				};
				Q.toString = function() {
					return ("duration: " + Q.duration + ", currentFrame: " + Q.currentFrame)
				};
				this.onTween.fire(Q);
				var P = this.runtimeAttributes;
				for ( var O in P) {
					this.setAttribute(O,
							this.doMethod(O, P[O].start, P[O].end), P[O].unit)
				}
				J += 1
			};
			var I = function() {
				var O = (new Date() - H) / 1000;
				var P = {
					duration : O,
					frames : J,
					fps : J / O
				};
				P.toString = function() {
					return ("duration: " + P.duration + ", frames: " + P.frames
							+ ", fps: " + P.fps)
				};
				F = false;
				J = 0;
				this.onComplete.fire(P)
			};
			this._onStart = new Ext.util.Event(this);
			this.onStart = new Ext.util.Event(this);
			this.onTween = new Ext.util.Event(this);
			this._onTween = new Ext.util.Event(this);
			this.onComplete = new Ext.util.Event(this);
			this._onComplete = new Ext.util.Event(this);
			this._onStart.addListener(N);
			this._onTween.addListener(M);
			this._onComplete.addListener(I)
		}
	};
	Ext.lib.AnimMgr = new function() {
		var G = null;
		var F = [];
		var E = 0;
		this.fps = 1000;
		this.delay = 1;
		this.registerElement = function(J) {
			F[F.length] = J;
			E += 1;
			J._onStart.fire();
			this.start()
		};
		this.unRegister = function(K, J) {
			K._onComplete.fire();
			J = J || I(K);
			if (J != -1) {
				F.splice(J, 1)
			}
			E -= 1;
			if (E <= 0) {
				this.stop()
			}
		};
		this.start = function() {
			if (G === null) {
				G = setInterval(this.run, this.delay)
			}
		};
		this.stop = function(L) {
			if (!L) {
				clearInterval(G);
				for ( var K = 0, J = F.length; K < J; ++K) {
					if (F[0].isAnimated()) {
						this.unRegister(F[0], 0)
					}
				}
				F = [];
				G = null;
				E = 0
			} else {
				this.unRegister(L)
			}
		};
		this.run = function() {
			for ( var L = 0, J = F.length; L < J; ++L) {
				var K = F[L];
				if (!K || !K.isAnimated()) {
					continue
				}
				if (K.currentFrame < K.totalFrames || K.totalFrames === null) {
					K.currentFrame += 1;
					if (K.useSeconds) {
						H(K)
					}
					K._onTween.fire()
				} else {
					Ext.lib.AnimMgr.stop(K, L)
				}
			}
		};
		var I = function(L) {
			for ( var K = 0, J = F.length; K < J; ++K) {
				if (F[K] == L) {
					return K
				}
			}
			return -1
		};
		var H = function(K) {
			var N = K.totalFrames;
			var M = K.currentFrame;
			var L = (K.currentFrame * K.duration * 1000 / K.totalFrames);
			var J = (new Date() - K.getStartTime());
			var O = 0;
			if (J < K.duration * 1000) {
				O = Math.round((J / L - 1) * K.currentFrame)
			} else {
				O = N - (M + 1)
			}
			if (O > 0 && isFinite(O)) {
				if (K.currentFrame + O >= N) {
					O = N - (M + 1)
				}
				K.currentFrame += O
			}
		}
	};
	Ext.lib.Bezier = new function() {
		this.getPosition = function(I, H) {
			var J = I.length;
			var G = [];
			for ( var F = 0; F < J; ++F) {
				G[F] = [ I[F][0], I[F][1] ]
			}
			for ( var E = 1; E < J; ++E) {
				for (F = 0; F < J - E; ++F) {
					G[F][0] = (1 - H) * G[F][0] + H * G[parseInt(F + 1, 10)][0];
					G[F][1] = (1 - H) * G[F][1] + H * G[parseInt(F + 1, 10)][1]
				}
			}
			return [ G[0][0], G[0][1] ]
		}
	};
	(function() {
		Ext.lib.ColorAnim = function(I, H, J, K) {
			Ext.lib.ColorAnim.superclass.constructor.call(this, I, H, J, K)
		};
		Ext.extend(Ext.lib.ColorAnim, Ext.lib.AnimBase);
		var F = Ext.lib;
		var G = F.ColorAnim.superclass;
		var E = F.ColorAnim.prototype;
		E.toString = function() {
			var H = this.getEl();
			var I = H.id || H.tagName;
			return ("ColorAnim " + I)
		};
		E.patterns.color = /color$/i;
		E.patterns.rgb = /^rgb\(([0-9]+)\s*,\s*([0-9]+)\s*,\s*([0-9]+)\)$/i;
		E.patterns.hex = /^#?([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})$/i;
		E.patterns.hex3 = /^#?([0-9A-F]{1})([0-9A-F]{1})([0-9A-F]{1})$/i;
		E.patterns.transparent = /^transparent|rgba\(0, 0, 0, 0\)$/;
		E.parseColor = function(H) {
			if (H.length == 3) {
				return H
			}
			var I = this.patterns.hex.exec(H);
			if (I && I.length == 4) {
				return [ parseInt(I[1], 16), parseInt(I[2], 16),
						parseInt(I[3], 16) ]
			}
			I = this.patterns.rgb.exec(H);
			if (I && I.length == 4) {
				return [ parseInt(I[1], 10), parseInt(I[2], 10),
						parseInt(I[3], 10) ]
			}
			I = this.patterns.hex3.exec(H);
			if (I && I.length == 4) {
				return [ parseInt(I[1] + I[1], 16), parseInt(I[2] + I[2], 16),
						parseInt(I[3] + I[3], 16) ]
			}
			return null
		};
		E.getAttribute = function(H) {
			var J = this.getEl();
			if (this.patterns.color.test(H)) {
				var K = C(J).getStyle(H);
				if (this.patterns.transparent.test(K)) {
					var I = J.parentNode;
					K = C(I).getStyle(H);
					while (I && this.patterns.transparent.test(K)) {
						I = I.parentNode;
						K = C(I).getStyle(H);
						if (I.tagName.toUpperCase() == "HTML") {
							K = "#fff"
						}
					}
				}
			} else {
				K = G.getAttribute.call(this, H)
			}
			return K
		};
		E.doMethod = function(I, M, J) {
			var L;
			if (this.patterns.color.test(I)) {
				L = [];
				for ( var K = 0, H = M.length; K < H; ++K) {
					L[K] = G.doMethod.call(this, I, M[K], J[K])
				}
				L = "rgb(" + Math.floor(L[0]) + "," + Math.floor(L[1]) + ","
						+ Math.floor(L[2]) + ")"
			} else {
				L = G.doMethod.call(this, I, M, J)
			}
			return L
		};
		E.setRuntimeAttribute = function(I) {
			G.setRuntimeAttribute.call(this, I);
			if (this.patterns.color.test(I)) {
				var K = this.attributes;
				var M = this.parseColor(this.runtimeAttributes[I].start);
				var J = this.parseColor(this.runtimeAttributes[I].end);
				if (typeof K[I]["to"] === "undefined"
						&& typeof K[I]["by"] !== "undefined") {
					J = this.parseColor(K[I].by);
					for ( var L = 0, H = M.length; L < H; ++L) {
						J[L] = M[L] + J[L]
					}
				}
				this.runtimeAttributes[I].start = M;
				this.runtimeAttributes[I].end = J
			}
		}
	})();
	Ext.lib.Easing = {
		easeNone : function(F, E, H, G) {
			return H * F / G + E
		},
		easeIn : function(F, E, H, G) {
			return H * (F /= G) * F + E
		},
		easeOut : function(F, E, H, G) {
			return -H * (F /= G) * (F - 2) + E
		},
		easeBoth : function(F, E, H, G) {
			if ((F /= G / 2) < 1) {
				return H / 2 * F * F + E
			}
			return -H / 2 * ((--F) * (F - 2) - 1) + E
		},
		easeInStrong : function(F, E, H, G) {
			return H * (F /= G) * F * F * F + E
		},
		easeOutStrong : function(F, E, H, G) {
			return -H * ((F = F / G - 1) * F * F * F - 1) + E
		},
		easeBothStrong : function(F, E, H, G) {
			if ((F /= G / 2) < 1) {
				return H / 2 * F * F * F * F + E
			}
			return -H / 2 * ((F -= 2) * F * F * F - 2) + E
		},
		elasticIn : function(G, E, K, J, F, I) {
			if (G == 0) {
				return E
			}
			if ((G /= J) == 1) {
				return E + K
			}
			if (!I) {
				I = J * 0.3
			}
			if (!F || F < Math.abs(K)) {
				F = K;
				var H = I / 4
			} else {
				var H = I / (2 * Math.PI) * Math.asin(K / F)
			}
			return -(F * Math.pow(2, 10 * (G -= 1)) * Math.sin((G * J - H)
					* (2 * Math.PI) / I))
					+ E
		},
		elasticOut : function(G, E, K, J, F, I) {
			if (G == 0) {
				return E
			}
			if ((G /= J) == 1) {
				return E + K
			}
			if (!I) {
				I = J * 0.3
			}
			if (!F || F < Math.abs(K)) {
				F = K;
				var H = I / 4
			} else {
				var H = I / (2 * Math.PI) * Math.asin(K / F)
			}
			return F * Math.pow(2, -10 * G)
					* Math.sin((G * J - H) * (2 * Math.PI) / I) + K + E
		},
		elasticBoth : function(G, E, K, J, F, I) {
			if (G == 0) {
				return E
			}
			if ((G /= J / 2) == 2) {
				return E + K
			}
			if (!I) {
				I = J * (0.3 * 1.5)
			}
			if (!F || F < Math.abs(K)) {
				F = K;
				var H = I / 4
			} else {
				var H = I / (2 * Math.PI) * Math.asin(K / F)
			}
			if (G < 1) {
				return -0.5
						* (F * Math.pow(2, 10 * (G -= 1)) * Math
								.sin((G * J - H) * (2 * Math.PI) / I)) + E
			}
			return F * Math.pow(2, -10 * (G -= 1))
					* Math.sin((G * J - H) * (2 * Math.PI) / I) * 0.5 + K + E
		},
		backIn : function(F, E, I, H, G) {
			if (typeof G == "undefined") {
				G = 1.70158
			}
			return I * (F /= H) * F * ((G + 1) * F - G) + E
		},
		backOut : function(F, E, I, H, G) {
			if (typeof G == "undefined") {
				G = 1.70158
			}
			return I * ((F = F / H - 1) * F * ((G + 1) * F + G) + 1) + E
		},
		backBoth : function(F, E, I, H, G) {
			if (typeof G == "undefined") {
				G = 1.70158
			}
			if ((F /= H / 2) < 1) {
				return I / 2 * (F * F * (((G *= (1.525)) + 1) * F - G)) + E
			}
			return I / 2 * ((F -= 2) * F * (((G *= (1.525)) + 1) * F + G) + 2)
					+ E
		},
		bounceIn : function(F, E, H, G) {
			return H - Ext.lib.Easing.bounceOut(G - F, 0, H, G) + E
		},
		bounceOut : function(F, E, H, G) {
			if ((F /= G) < (1 / 2.75)) {
				return H * (7.5625 * F * F) + E
			} else {
				if (F < (2 / 2.75)) {
					return H * (7.5625 * (F -= (1.5 / 2.75)) * F + 0.75) + E
				} else {
					if (F < (2.5 / 2.75)) {
						return H * (7.5625 * (F -= (2.25 / 2.75)) * F + 0.9375)
								+ E
					}
				}
			}
			return H * (7.5625 * (F -= (2.625 / 2.75)) * F + 0.984375) + E
		},
		bounceBoth : function(F, E, H, G) {
			if (F < G / 2) {
				return Ext.lib.Easing.bounceIn(F * 2, 0, H, G) * 0.5 + E
			}
			return Ext.lib.Easing.bounceOut(F * 2 - G, 0, H, G) * 0.5 + H * 0.5
					+ E
		}
	};
	(function() {
		Ext.lib.Motion = function(K, J, L, M) {
			if (K) {
				Ext.lib.Motion.superclass.constructor.call(this, K, J, L, M)
			}
		};
		Ext.extend(Ext.lib.Motion, Ext.lib.ColorAnim);
		var H = Ext.lib;
		var I = H.Motion.superclass;
		var F = H.Motion.prototype;
		F.toString = function() {
			var J = this.getEl();
			var K = J.id || J.tagName;
			return ("Motion " + K)
		};
		F.patterns.points = /^points$/i;
		F.setAttribute = function(J, L, K) {
			if (this.patterns.points.test(J)) {
				K = K || "px";
				I.setAttribute.call(this, "left", L[0], K);
				I.setAttribute.call(this, "top", L[1], K)
			} else {
				I.setAttribute.call(this, J, L, K)
			}
		};
		F.getAttribute = function(J) {
			if (this.patterns.points.test(J)) {
				var K = [ I.getAttribute.call(this, "left"),
						I.getAttribute.call(this, "top") ]
			} else {
				K = I.getAttribute.call(this, J)
			}
			return K
		};
		F.doMethod = function(J, N, K) {
			var M = null;
			if (this.patterns.points.test(J)) {
				var L = this
						.method(this.currentFrame, 0, 100, this.totalFrames) / 100;
				M = H.Bezier.getPosition(this.runtimeAttributes[J], L)
			} else {
				M = I.doMethod.call(this, J, N, K)
			}
			return M
		};
		F.setRuntimeAttribute = function(S) {
			if (this.patterns.points.test(S)) {
				var K = this.getEl();
				var M = this.attributes;
				var J;
				var O = M["points"]["control"] || [];
				var L;
				var P, R;
				if (O.length > 0 && !Ext.isArray(O[0])) {
					O = [ O ]
				} else {
					var N = [];
					for (P = 0, R = O.length; P < R; ++P) {
						N[P] = O[P]
					}
					O = N
				}
				Ext.fly(K).position();
				if (G(M["points"]["from"])) {
					Ext.lib.Dom.setXY(K, M["points"]["from"])
				} else {
					Ext.lib.Dom.setXY(K, Ext.lib.Dom.getXY(K))
				}
				J = this.getAttribute("points");
				if (G(M["points"]["to"])) {
					L = E.call(this, M["points"]["to"], J);
					var Q = Ext.lib.Dom.getXY(this.getEl());
					for (P = 0, R = O.length; P < R; ++P) {
						O[P] = E.call(this, O[P], J)
					}
				} else {
					if (G(M["points"]["by"])) {
						L = [ J[0] + M["points"]["by"][0],
								J[1] + M["points"]["by"][1] ];
						for (P = 0, R = O.length; P < R; ++P) {
							O[P] = [ J[0] + O[P][0], J[1] + O[P][1] ]
						}
					}
				}
				this.runtimeAttributes[S] = [ J ];
				if (O.length > 0) {
					this.runtimeAttributes[S] = this.runtimeAttributes[S]
							.concat(O)
				}
				this.runtimeAttributes[S][this.runtimeAttributes[S].length] = L
			} else {
				I.setRuntimeAttribute.call(this, S)
			}
		};
		var E = function(J, L) {
			var K = Ext.lib.Dom.getXY(this.getEl());
			J = [ J[0] - K[0] + L[0], J[1] - K[1] + L[1] ];
			return J
		};
		var G = function(J) {
			return (typeof J !== "undefined")
		}
	})();
	(function() {
		Ext.lib.Scroll = function(I, H, J, K) {
			if (I) {
				Ext.lib.Scroll.superclass.constructor.call(this, I, H, J, K)
			}
		};
		Ext.extend(Ext.lib.Scroll, Ext.lib.ColorAnim);
		var F = Ext.lib;
		var G = F.Scroll.superclass;
		var E = F.Scroll.prototype;
		E.toString = function() {
			var H = this.getEl();
			var I = H.id || H.tagName;
			return ("Scroll " + I)
		};
		E.doMethod = function(H, K, I) {
			var J = null;
			if (H == "scroll") {
				J = [
						this.method(this.currentFrame, K[0], I[0] - K[0],
								this.totalFrames),
						this.method(this.currentFrame, K[1], I[1] - K[1],
								this.totalFrames) ]
			} else {
				J = G.doMethod.call(this, H, K, I)
			}
			return J
		};
		E.getAttribute = function(H) {
			var J = null;
			var I = this.getEl();
			if (H == "scroll") {
				J = [ I.scrollLeft, I.scrollTop ]
			} else {
				J = G.getAttribute.call(this, H)
			}
			return J
		};
		E.setAttribute = function(H, K, J) {
			var I = this.getEl();
			if (H == "scroll") {
				I.scrollLeft = K[0];
				I.scrollTop = K[1]
			} else {
				G.setAttribute.call(this, H, K, J)
			}
		}
	})()
})();
