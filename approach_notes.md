First tried to use docker for environment reproducibility.

However, Docker runs in the context of linux so the build Jenkins Agent ended up running as Linux, which is not what we want. To get Jenkins to use Mac under Docker introduced more complexity than deemed worth it. Hence, refactored to use Jenkins on Mac directly.
