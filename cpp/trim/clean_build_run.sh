(rm -rf ./build; rm /usr/local/bin/trim-example-app; echo "cleaned") \
&& cmake -S . -B ./build \
&& cd build \
&& make install \
&& trim-example-app first second
